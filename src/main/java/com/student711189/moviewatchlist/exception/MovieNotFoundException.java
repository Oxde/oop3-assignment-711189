package com.student711189.moviewatchlist.exception;

/**
 * Exception thrown when a movie is not found
 */
public class MovieNotFoundException extends RuntimeException {
    
    public MovieNotFoundException(String message) {
        super(message);
    }
    
    public MovieNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 