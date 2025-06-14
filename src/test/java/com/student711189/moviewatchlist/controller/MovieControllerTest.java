package com.student711189.moviewatchlist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.student711189.moviewatchlist.model.AddMovieRequest;
import com.student711189.moviewatchlist.model.MovieDto;
import com.student711189.moviewatchlist.model.UpdateMovieRequest;
import com.student711189.moviewatchlist.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for MovieController
 * Tests REST endpoints using MockMvc
 */
@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    private MovieDto testMovieDto;
    private AddMovieRequest addMovieRequest;
    private UpdateMovieRequest updateMovieRequest;

    @BeforeEach
    void setUp() {
        testMovieDto = new MovieDto();
        testMovieDto.setId(1L);
        testMovieDto.setTitle("Test Movie");
        testMovieDto.setYear("2023");
        testMovieDto.setGenre("Action");
        testMovieDto.setDirector("Test Director");
        testMovieDto.setPlot("Test plot");
        testMovieDto.setWatched(false);
        testMovieDto.setRating(null);

        addMovieRequest = new AddMovieRequest("Test Movie", "2023");
        updateMovieRequest = new UpdateMovieRequest(true, 4);
    }

    @Test
    void addMovie_WhenValidRequest_ShouldReturnCreated() throws Exception {
        // Arrange
        when(movieService.addMovie(anyString(), anyString())).thenReturn(testMovieDto);

        // Act & Assert
        mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addMovieRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Movie"))
                .andExpect(jsonPath("$.year").value("2023"));

        verify(movieService).addMovie("Test Movie", "2023");
    }

    @Test
    void addMovie_WhenMovieExists_ShouldReturnConflict() throws Exception {
        // Arrange
        when(movieService.addMovie(anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Movie already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addMovieRequest)))
                .andExpect(status().isConflict());

        verify(movieService).addMovie("Test Movie", "2023");
    }

    @Test
    void getMovies_WhenValidRequest_ShouldReturnPagedMovies() throws Exception {
        // Arrange
        List<MovieDto> movies = Arrays.asList(testMovieDto);
        Page<MovieDto> moviePage = new PageImpl<>(movies, PageRequest.of(0, 10), 1);
        
        when(movieService.getMovies(any())).thenReturn(moviePage);

        // Act & Assert
        mockMvc.perform(get("/api/movies")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test Movie"))
                .andExpect(jsonPath("$.content[0].year").value("2023"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.size").value(10));

        verify(movieService).getMovies(any());
    }

    @Test
    void getMovies_WithoutParameters_ShouldUseDefaults() throws Exception {
        // Arrange
        List<MovieDto> movies = Arrays.asList(testMovieDto);
        Page<MovieDto> moviePage = new PageImpl<>(movies, PageRequest.of(0, 10), 1);
        
        when(movieService.getMovies(any())).thenReturn(moviePage);

        // Act & Assert
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk());

        verify(movieService).getMovies(any());
    }

    @Test
    void updateMovie_WhenValidRequest_ShouldReturnUpdatedMovie() throws Exception {
        // Arrange
        testMovieDto.setWatched(true);
        testMovieDto.setRating(4);
        
        when(movieService.updateMovie(eq(1L), eq(true), eq(4))).thenReturn(testMovieDto);

        // Act & Assert
        mockMvc.perform(put("/api/movies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateMovieRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.watched").value(true))
                .andExpect(jsonPath("$.rating").value(4));

        verify(movieService).updateMovie(1L, true, 4);
    }

    @Test
    void updateMovie_WhenMovieNotFound_ShouldReturnBadRequest() throws Exception {
        // Arrange
        when(movieService.updateMovie(anyLong(), any(), any()))
                .thenThrow(new IllegalArgumentException("Movie not found"));

        // Act & Assert
        mockMvc.perform(put("/api/movies/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateMovieRequest)))
                .andExpect(status().isBadRequest());

        verify(movieService).updateMovie(999L, true, 4);
    }

    @Test
    void deleteMovie_WhenValidId_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(movieService).deleteMovie(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/movies/1"))
                .andExpect(status().isNoContent());

        verify(movieService).deleteMovie(1L);
    }

    @Test
    void deleteMovie_WhenMovieNotFound_ShouldReturnNotFound() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("Movie not found"))
                .when(movieService).deleteMovie(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/movies/999"))
                .andExpect(status().isNotFound());

        verify(movieService).deleteMovie(999L);
    }

    @Test
    void getMovie_ShouldReturnNotImplemented() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/movies/1"))
                .andExpect(status().isNotImplemented());
    }
} 