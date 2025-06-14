package com.student711189.moviewatchlist.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Movie entity representing a movie in the watchlist
 * Contains movie details from OMDb API and image paths from TMDb API
 */
@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String year;

    @Column
    private String genre;

    @Column
    private String director;

    @Column(length = 1000)
    private String plot;

    @Column(name = "watched", nullable = false)
    private boolean watched = false;

    @Column(name = "rating")
    private Integer rating; // 1-5 scale, null if not rated

    @ElementCollection
    @CollectionTable(name = "movie_images", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "image_path")
    private List<String> imagePaths;

    @Column(name = "poster_url")
    private String posterUrl; // Original poster URL from OMDb

    /**
     * Constructor for creating a new movie with essential data
     */
    public Movie(String title, String year, String genre, String director, String plot, String posterUrl) {
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.director = director;
        this.plot = plot;
        this.posterUrl = posterUrl;
        this.watched = false;
        this.rating = null;
    }
} 