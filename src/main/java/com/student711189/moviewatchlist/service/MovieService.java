package com.student711189.moviewatchlist.service;

import com.student711189.moviewatchlist.exception.MovieAlreadyExistsException;
import com.student711189.moviewatchlist.exception.MovieNotFoundException;
import com.student711189.moviewatchlist.model.*;
import com.student711189.moviewatchlist.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class for Movie operations
 * Handles business logic, external API calls, and multi-threading
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    @Value("${omdb.api.key}")
    private String omdbApiKey;

    @Value("${omdb.api.url}")
    private String omdbApiUrl;

    @Value("${tmdb.api.key}")
    private String tmdbApiKey;

    @Value("${tmdb.api.url}")
    private String tmdbApiUrl;

    @Value("${app.images.directory:images}")
    private String imagesDirectory;

    // TMDb image base URL
    private static final String TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    /**
     * Add a new movie to the watchlist
     * Fetches data from OMDb and images from TMDb using multi-threading
     */
    public MovieDto addMovie(String title, String year) {
        log.info("Adding movie: {} ({})", title, year);
        
        // Check if movie already exists
        if (movieRepository.findByTitleIgnoreCase(title).isPresent()) {
            throw new MovieAlreadyExistsException("Movie already exists");
        }
        
        // Fetch movie data from OMDb API
        OmdbResponse omdbData = fetchMovieFromOmdb(title, year);
        if (omdbData.hasError()) {
            throw new MovieNotFoundException("Movie not found on OMDb: " + omdbData.getError());
        }
        
        // Create Movie entity from OMDb data
        Movie movie = createMovieFromOmdbData(omdbData);
        
        // Save movie first to get an ID for image naming
        Movie savedMovie = movieRepository.save(movie);
        
        // Fetch and download images from TMDb in parallel
        List<String> imagePaths = fetchAndDownloadImages(title, savedMovie.getId());
        savedMovie.setImagePaths(imagePaths);
        
        // Update movie with image paths
        savedMovie = movieRepository.save(savedMovie);
        
        log.info("Movie added successfully: {}", savedMovie.getTitle());
        return MovieDto.fromEntity(savedMovie);
    }

    /**
     * Fetch movie data from OMDb API
     */
    private OmdbResponse fetchMovieFromOmdb(String title, String year) {
        String url = String.format("%s?t=%s&y=%s&apikey=%s", 
            omdbApiUrl, title, year != null ? year : "", omdbApiKey);
        
        try {
            log.info("Fetching movie from OMDb: {} ({})", title, year);
            return restTemplate.getForObject(url, OmdbResponse.class);
        } catch (Exception e) {
            log.error("Error fetching movie from OMDb: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch movie from OMDb", e);
        }
    }

    /**
     * Create Movie entity from OMDb response data
     */
    private Movie createMovieFromOmdbData(OmdbResponse omdbData) {
        Movie movie = new Movie();
        movie.setTitle(omdbData.getTitle());
        movie.setYear(omdbData.getYear());
        movie.setGenre(omdbData.getGenre());
        movie.setDirector(omdbData.getDirector());
        movie.setPlot(omdbData.getPlot());
        movie.setPosterUrl(omdbData.getPoster());
        movie.setWatched(false);
        movie.setRating(0);
        
        return movie;
    }

    /**
     * Fetch images from TMDb and download them in parallel
     */
    private List<String> fetchAndDownloadImages(String title, Long movieId) {
        try {
            // Search for movie on TMDb
            TmdbSearchResponse searchResponse = searchMovieOnTmdb(title);
            if (searchResponse.getFirstResult() == null) {
                log.warn("Movie not found on TMDb: {}", title);
                return new ArrayList<>();
            }
            
            Long tmdbMovieId = searchResponse.getFirstResult().getId();
            
            // Get movie images
            TmdbImagesResponse imagesResponse = getMovieImages(tmdbMovieId);
            
            // Collect up to 3 image URLs using Java Streams
            List<String> imageUrls = Stream.concat(
                    imagesResponse.getPosters() != null ? 
                        imagesResponse.getPosters().stream().map(img -> TMDB_IMAGE_BASE_URL + img.getFilePath()) : 
                        Stream.empty(),
                    imagesResponse.getBackdrops() != null ? 
                        imagesResponse.getBackdrops().stream().map(img -> TMDB_IMAGE_BASE_URL + img.getFilePath()) : 
                        Stream.empty()
                )
                .filter(url -> url != null && !url.isEmpty())
                .limit(3)
                .collect(Collectors.toList());
            
            if (imageUrls.isEmpty()) {
                log.warn("No images found for movie: {}", title);
                return new ArrayList<>();
            }
            
            // Download images in parallel using virtual threads
            ensureImagesDirectoryExists();
            
            List<CompletableFuture<String>> downloadFutures = new ArrayList<>();
            for (int i = 0; i < imageUrls.size(); i++) {
                String imageUrl = imageUrls.get(i);
                String fileName = String.format("movie_%d_image_%d.jpg", movieId, i + 1);
                
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        downloadImage(imageUrl, fileName);
                        return fileName;
                    } catch (Exception e) {
                        log.error("Failed to download image: {}", imageUrl, e);
                        return null;
                    }
                }, executor);
                
                downloadFutures.add(future);
            }
            
            // Wait for all downloads to complete and collect successful downloads
            return downloadFutures.stream()
                .map(CompletableFuture::join)
                .filter(fileName -> fileName != null)
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            log.error("Error fetching images from TMDb for movie: {}", title, e);
            return new ArrayList<>();
        }
    }

    /**
     * Search for movie on TMDb
     */
    private TmdbSearchResponse searchMovieOnTmdb(String title) {
        String url = String.format("%s/search/movie?query=%s&api_key=%s", tmdbApiUrl, title, tmdbApiKey);
        
        return restTemplate.getForObject(url, TmdbSearchResponse.class);
    }

    /**
     * Get movie images from TMDb
     */
    private TmdbImagesResponse getMovieImages(Long movieId) {
        String url = String.format("%s/movie/%d/images?api_key=%s", tmdbApiUrl, movieId, tmdbApiKey);
        
        return restTemplate.getForObject(url, TmdbImagesResponse.class);
    }

    /**
     * Download an image from URL to local file using Java I/O streams
     */
    private void downloadImage(String imageUrl, String fileName) {
        Path filePath = Paths.get(imagesDirectory, fileName);
        
        try (InputStream in = new URL(imageUrl).openStream();
             OutputStream out = Files.newOutputStream(filePath)) {
            
            in.transferTo(out); // Java 9+ method for efficient stream copying
            log.info("Downloaded image: {} -> {}", imageUrl, fileName);
            
        } catch (IOException e) {
            log.error("Failed to download image: {} -> {}", imageUrl, fileName, e);
            throw new RuntimeException("Image download failed", e);
        }
    }

    /**
     * Get all movies with pagination
     */
    public Page<MovieDto> getMovies(Pageable pageable) {
        log.info("Fetching movies - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        Page<Movie> movies = movieRepository.findAll(pageable);
        return movies.map(MovieDto::fromEntity);
    }

    /**
     * Get a movie by ID
     */
    public MovieDto getMovie(Long id) {
        log.info("Fetching movie with id: {}", id);
        
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new MovieNotFoundException("Movie not found"));
        
        return MovieDto.fromEntity(movie);
    }

    /**
     * Update movie watched status and rating
     */
    public MovieDto updateMovie(Long id, Boolean watched, Integer rating) {
        log.info("Updating movie {} - watched: {}, rating: {}", id, watched, rating);
        
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new MovieNotFoundException("Movie not found"));
        
        if (watched != null) {
            movie.setWatched(watched);
        }
        if (rating != null) {
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }
            movie.setRating(rating);
        }
        
        Movie savedMovie = movieRepository.save(movie);
        log.info("Movie updated successfully: {}", savedMovie.getTitle());
        
        return MovieDto.fromEntity(savedMovie);
    }

    /**
     * Delete a movie from the watchlist
     */
    public void deleteMovie(Long id) {
        log.info("Deleting movie with id: {}", id);
        
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new MovieNotFoundException("Movie not found"));
        
        // Delete image files
        deleteMovieImages(movie.getImagePaths());
        
        movieRepository.delete(movie);
        log.info("Movie deleted successfully: {}", movie.getTitle());
    }

    /**
     * Delete image files from filesystem
     */
    private void deleteMovieImages(List<String> imagePaths) {
        if (imagePaths == null || imagePaths.isEmpty()) {
            return;
        }
        
        for (String imagePath : imagePaths) {
            try {
                Path filePath = Paths.get(imagesDirectory, imagePath);
                Files.deleteIfExists(filePath);
                log.info("Deleted image file: {}", imagePath);
            } catch (IOException e) {
                log.error("Failed to delete image file: {}", imagePath, e);
            }
        }
    }

    /**
     * Create images directory if it doesn't exist
     */
    private void ensureImagesDirectoryExists() {
        try {
            Path dirPath = Paths.get(imagesDirectory);
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            log.error("Failed to create images directory: {}", imagesDirectory, e);
            throw new RuntimeException("Could not create images directory", e);
        }
    }
} 