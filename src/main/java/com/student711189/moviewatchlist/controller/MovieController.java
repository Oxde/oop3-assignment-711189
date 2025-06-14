package com.student711189.moviewatchlist.controller;

import com.student711189.moviewatchlist.model.AddMovieRequest;
import com.student711189.moviewatchlist.model.MovieDto;
import com.student711189.moviewatchlist.model.UpdateMovieRequest;
import com.student711189.moviewatchlist.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Movie Watchlist operations
 * Provides endpoints for CRUD operations on movies
 */
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final MovieService movieService;

    /**
     * Add a new movie to the watchlist
     * POST /api/movies
     */
    @PostMapping
    public ResponseEntity<MovieDto> addMovie(@RequestBody AddMovieRequest request) {
        log.info("Adding movie: {}", request.getTitle());
        
        try {
            MovieDto movie = movieService.addMovie(request.getTitle(), request.getYear());
            return ResponseEntity.status(HttpStatus.CREATED).body(movie);
        } catch (IllegalArgumentException e) {
            log.error("Error adding movie: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            log.error("Unexpected error adding movie", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get paginated list of movies
     * GET /api/movies?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<MovieDto>> getMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("Fetching movies - page: {}, size: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<MovieDto> movies = movieService.getMovies(pageable);
        
        return ResponseEntity.ok(movies);
    }

    /**
     * Update movie watched status and/or rating
     * PUT /api/movies/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovie(
            @PathVariable Long id,
            @RequestBody UpdateMovieRequest request) {
        
        log.info("Updating movie {} - watched: {}, rating: {}", 
                id, request.getWatched(), request.getRating());
        
        try {
            MovieDto updatedMovie = movieService.updateMovie(
                    id, request.getWatched(), request.getRating());
            return ResponseEntity.ok(updatedMovie);
        } catch (IllegalArgumentException e) {
            log.error("Error updating movie: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Unexpected error updating movie", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete a movie from the watchlist
     * DELETE /api/movies/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        log.info("Deleting movie with id: {}", id);
        
        try {
            movieService.deleteMovie(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error deleting movie: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Unexpected error deleting movie", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get a specific movie by ID
     * GET /api/movies/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovie(@PathVariable Long id) {
        log.info("Fetching movie with id: {}", id);
        
        // TODO: Implement get movie by ID in service
        // For now, return not implemented
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
} 