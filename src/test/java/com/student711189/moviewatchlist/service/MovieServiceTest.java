package com.student711189.moviewatchlist.service;

import com.student711189.moviewatchlist.model.Movie;
import com.student711189.moviewatchlist.model.MovieDto;
import com.student711189.moviewatchlist.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for MovieService
 * Tests business logic in isolation using mocks
 */
@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie testMovie;
    private MovieDto testMovieDto;

    @BeforeEach
    void setUp() {
        // Set up test data
        testMovie = new Movie();
        testMovie.setId(1L);
        testMovie.setTitle("Test Movie");
        testMovie.setYear("2023");
        testMovie.setGenre("Action");
        testMovie.setDirector("Test Director");
        testMovie.setPlot("Test plot");
        testMovie.setWatched(false);
        testMovie.setRating(null);

        testMovieDto = MovieDto.fromEntity(testMovie);

        // Set up configuration values
        ReflectionTestUtils.setField(movieService, "omdbApiKey", "test-omdb-key");
        ReflectionTestUtils.setField(movieService, "tmdbApiKey", "test-tmdb-key");
        ReflectionTestUtils.setField(movieService, "imagesDirectory", "test-images");
    }

    @Test
    void addMovie_WhenMovieDoesNotExist_ShouldAddMovie() {
        // Arrange
        String title = "New Movie";
        String year = "2023";
        
        when(movieRepository.existsByTitleIgnoreCase(title)).thenReturn(false);
        when(movieRepository.save(any(Movie.class))).thenReturn(testMovie);

        // Act
        MovieDto result = movieService.addMovie(title, year);

        // Assert
        assertNotNull(result);
        assertEquals(testMovie.getTitle(), result.getTitle());
        verify(movieRepository).existsByTitleIgnoreCase(title);
        verify(movieRepository).save(any(Movie.class));
    }

    @Test
    void addMovie_WhenMovieAlreadyExists_ShouldThrowException() {
        // Arrange
        String title = "Existing Movie";
        String year = "2023";
        
        when(movieRepository.existsByTitleIgnoreCase(title)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> movieService.addMovie(title, year)
        );
        
        assertEquals("Movie already exists in watchlist: " + title, exception.getMessage());
        verify(movieRepository).existsByTitleIgnoreCase(title);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void getMovies_ShouldReturnPaginatedMovies() {
        // Arrange
        List<Movie> movies = Arrays.asList(testMovie);
        Page<Movie> moviePage = new PageImpl<>(movies);
        Pageable pageable = PageRequest.of(0, 10);
        
        when(movieRepository.findAll(pageable)).thenReturn(moviePage);

        // Act
        Page<MovieDto> result = movieService.getMovies(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testMovie.getTitle(), result.getContent().get(0).getTitle());
        verify(movieRepository).findAll(pageable);
    }

    @Test
    void updateMovie_WhenMovieExists_ShouldUpdateMovie() {
        // Arrange
        Long movieId = 1L;
        Boolean watched = true;
        Integer rating = 4;
        
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(testMovie));
        when(movieRepository.save(any(Movie.class))).thenReturn(testMovie);

        // Act
        MovieDto result = movieService.updateMovie(movieId, watched, rating);

        // Assert
        assertNotNull(result);
        verify(movieRepository).findById(movieId);
        verify(movieRepository).save(any(Movie.class));
    }

    @Test
    void updateMovie_WhenMovieNotFound_ShouldThrowException() {
        // Arrange
        Long movieId = 999L;
        
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> movieService.updateMovie(movieId, true, 5)
        );
        
        assertEquals("Movie not found with id: " + movieId, exception.getMessage());
        verify(movieRepository).findById(movieId);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void updateMovie_WhenInvalidRating_ShouldThrowException() {
        // Arrange
        Long movieId = 1L;
        Integer invalidRating = 6;
        
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(testMovie));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> movieService.updateMovie(movieId, true, invalidRating)
        );
        
        assertEquals("Rating must be between 1 and 5", exception.getMessage());
        verify(movieRepository).findById(movieId);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void deleteMovie_WhenMovieExists_ShouldDeleteMovie() {
        // Arrange
        Long movieId = 1L;
        
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(testMovie));

        // Act
        movieService.deleteMovie(movieId);

        // Assert
        verify(movieRepository).findById(movieId);
        verify(movieRepository).deleteById(movieId);
    }

    @Test
    void deleteMovie_WhenMovieNotFound_ShouldThrowException() {
        // Arrange
        Long movieId = 999L;
        
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> movieService.deleteMovie(movieId)
        );
        
        assertEquals("Movie not found with id: " + movieId, exception.getMessage());
        verify(movieRepository).findById(movieId);
        verify(movieRepository, never()).deleteById(any());
    }
} 