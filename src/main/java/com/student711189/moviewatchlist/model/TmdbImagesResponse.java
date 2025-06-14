package com.student711189.moviewatchlist.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * DTO for TMDb Images API response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmdbImagesResponse {

    @JsonProperty("posters")
    private List<TmdbImage> posters;

    @JsonProperty("backdrops")
    private List<TmdbImage> backdrops;

    /**
     * Individual image from TMDb
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TmdbImage {
        
        @JsonProperty("file_path")
        private String filePath;

        @JsonProperty("width")
        private int width;

        @JsonProperty("height")
        private int height;

        @JsonProperty("vote_average")
        private double voteAverage;
    }
} 