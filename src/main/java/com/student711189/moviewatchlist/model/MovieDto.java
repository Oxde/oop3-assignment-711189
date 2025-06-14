package com.student711189.moviewatchlist.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for Movie
 * Used for API responses to avoid exposing internal entity structure
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

    private Long id;
    private String title;
    private String year;
    private String genre;
    private String director;
    private String plot;
    private boolean watched;
    private Integer rating;
    private List<String> imagePaths;
    private String posterUrl;

    /**
     * Convert Movie entity to DTO
     */
    public static MovieDto fromEntity(Movie movie) {
        return new MovieDto(
            movie.getId(),
            movie.getTitle(),
            movie.getYear(),
            movie.getGenre(),
            movie.getDirector(),
            movie.getPlot(),
            movie.isWatched(),
            movie.getRating(),
            movie.getImagePaths(),
            movie.getPosterUrl()
        );
    }
} 