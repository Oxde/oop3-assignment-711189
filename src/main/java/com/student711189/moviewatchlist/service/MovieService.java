package com.student711189.moviewatchlist.service;

import com.student711189.moviewatchlist.model.Movie;
import com.student711189.moviewatchlist.model.MovieDto;
import com.student711189.moviewatchlist.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Value("${omdb.api.key:YOUR_OMDB_KEY_HERE}")
    private String omdbApiKey;

    @Value("${tmdb.api.key:YOUR_TMDB_KEY_HERE}")
    private String tmdbApiKey;

    @Value("${omdb.api.url:http://www.omdbapi.com/}")
    private String omdbApiUrl;

    @Value("${tmdb.api.url:https://api.themoviedb.org/3}")
    private String tmdbApiUrl;

    @Value("${app.images.directory:images}")
    private String imagesDirectory;

    /**
     * Add a new movie to the watchlist
     * Fetches data from OMDb API, images from TMDb API, downloads images concurrently
     */
    public MovieDto addMovie(String title, String year) {
        log.info("Adding movie: {} ({})", title, year);
        
        // Check if movie already exists
        if (movieRepository.existsByTitleIgnoreCase(title)) {
            throw new IllegalArgumentException("Movie already exists in watchlist: " + title);
        }

        // TODO: Implement OMDb API call to fetch movie details
        // TODO: Implement TMDb API call to fetch image URLs
        // TODO: Implement concurrent image downloading using ExecutorService
        // TODO: Create Movie entity and save to database
        
        // Placeholder implementation
        Movie movie = new Movie(title, year, "Genre", "Director", "Plot", "posterUrl");
        Movie savedMovie = movieRepository.save(movie);
        
        log.info("Movie added successfully: {}", savedMovie.getTitle());
        return MovieDto.fromEntity(savedMovie);
    }

    /**
     * Get paginated list of movies
     */
    public Page<MovieDto> getMovies(Pageable pageable) {
        log.info("Fetching movies - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        Page<Movie> movies = movieRepository.findAll(pageable);
        
        // Use Java Streams to convert entities to DTOs
        return movies.map(MovieDto::fromEntity);
    }

    /**
     * Update movie watched status and rating
     */
    public MovieDto updateMovie(Long id, Boolean watched, Integer rating) {
        log.info("Updating movie {} - watched: {}, rating: {}", id, watched, rating);
        
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));

        if (watched != null) {
            movie.setWatched(watched);
        }
        
        if (rating != null) {
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }
            movie.setRating(rating);
        }

        Movie updatedMovie = movieRepository.save(movie);
        log.info("Movie updated successfully: {}", updatedMovie.getTitle());
        
        return MovieDto.fromEntity(updatedMovie);
    }

    /**
     * Delete movie and its associated images
     */
    public void deleteMovie(Long id) {
        log.info("Deleting movie with id: {}", id);
        
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));

        // TODO: Implement image file deletion
        deleteMovieImages(movie);
        
        movieRepository.deleteById(id);
        log.info("Movie deleted successfully: {}", movie.getTitle());
    }

    /**
     * Delete image files associated with a movie
     */
    private void deleteMovieImages(Movie movie) {
        if (movie.getImagePaths() != null) {
            movie.getImagePaths().forEach(imagePath -> {
                try {
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        boolean deleted = imageFile.delete();
                        log.info("Image file {} deleted: {}", imagePath, deleted);
                    }
                } catch (Exception e) {
                    log.error("Error deleting image file: {}", imagePath, e);
                }
            });
        }
    }

    // TODO: Implement private methods for:
    // - fetchMovieFromOmdb(String title, String year)
    // - fetchImageUrlsFromTmdb(String title)
    // - downloadImagesaConcurrently(List<String> imageUrls, String movieTitle)
    // - downloadImage(String imageUrl, String fileName)
} 