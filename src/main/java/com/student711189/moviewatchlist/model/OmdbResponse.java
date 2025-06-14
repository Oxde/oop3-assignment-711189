package com.student711189.moviewatchlist.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for OMDb API response
 * Maps JSON fields from OMDb API to Java objects
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OmdbResponse {

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("Genre")
    private String genre;

    @JsonProperty("Director")
    private String director;

    @JsonProperty("Plot")
    private String plot;

    @JsonProperty("Poster")
    private String poster;

    @JsonProperty("Response")
    private String response;

    @JsonProperty("Error")
    private String error;

    @JsonProperty("imdbID")
    private String imdbId;

    /**
     * Check if the response indicates success
     */
    public boolean isSuccess() {
        return "True".equalsIgnoreCase(response);
    }

    /**
     * Check if the response indicates an error
     */
    public boolean hasError() {
        return "False".equalsIgnoreCase(response) || error != null;
    }
} 