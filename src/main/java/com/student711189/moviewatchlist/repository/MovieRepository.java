package com.student711189.moviewatchlist.repository;

import com.student711189.moviewatchlist.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Movie entity
 * Provides CRUD operations and pagination support
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * Find movie by title (case-insensitive)
     * Useful for checking duplicates
     */
    Optional<Movie> findByTitleIgnoreCase(String title);

    /**
     * Find all movies with pagination
     * Inherited from JpaRepository: findAll(Pageable pageable)
     */
    Page<Movie> findAll(Pageable pageable);

    /**
     * Check if movie exists by title
     */
    boolean existsByTitleIgnoreCase(String title);
} 