package com.student711189.moviewatchlist.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Request DTO for updating movie watched status and rating
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMovieRequest {
    
    private Boolean watched;
    private Integer rating; // 1-5 scale, null if not rated
} 