package com.student711189.moviewatchlist.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * DTO for TMDb Search API response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmdbSearchResponse {

    @JsonProperty("results")
    private List<TmdbMovieResult> results;

    @JsonProperty("total_results")
    private int totalResults;

    /**
     * Get the first movie result if available
     */
    public TmdbMovieResult getFirstResult() {
        return results != null && !results.isEmpty() ? results.get(0) : null;
    }

    /**
     * Individual movie result from TMDb search
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TmdbMovieResult {
        
        @JsonProperty("id")
        private Long id;

        @JsonProperty("title")
        private String title;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonProperty("backdrop_path")
        private String backdropPath;

        @JsonProperty("release_date")
        private String releaseDate;
    }
} 