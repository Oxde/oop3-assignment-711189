package com.student711189.moviewatchlist.exception;

/**
 * Exception thrown when attempting to add a movie that already exists
 */
public class MovieAlreadyExistsException extends RuntimeException {
    
    public MovieAlreadyExistsException(String message) {
        super(message);
    }
    
    public MovieAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
} 