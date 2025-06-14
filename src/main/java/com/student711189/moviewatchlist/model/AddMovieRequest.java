package com.student711189.moviewatchlist.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Request DTO for adding a new movie to the watchlist
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddMovieRequest {
    
    private String title;
    private String year; // Optional, for more precise search
    
    public AddMovieRequest(String title) {
        this.title = title;
    }
} 