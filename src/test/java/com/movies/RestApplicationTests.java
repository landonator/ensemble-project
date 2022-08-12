/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.movies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.model.Movie;
import com.movies.model.MovieRequest;
import com.movies.repositories.MovieRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class RestApplicationTests {

    private static final String API_VER = "/api/v1";

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        movieRepository.deleteAll();
    }

    @Test
    void createAndGetTest() throws Exception {
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setTitle("Heat");
        movieRequest.setDescription("A group of high-end professional thieves start to feel the heat from the LAPD " +
                "when they unknowingly leave a clue at their latest heist.");
        movieRequest.setRating(8.3);
        movieRequest.setReleaseYear(1995);
        // Don't set duration here to do a quick validation test

        String movieAsString = objectMapper.writeValueAsString(movieRequest);

        // First, test validation
        mockMvc.perform(post(API_VER + "/movies")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieAsString))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


        // Set duration now
        movieRequest.setDuration(165);
        movieAsString = objectMapper.writeValueAsString(movieRequest);

        String entityLocation = createAndValidate(movieAsString);

        mockMvc.perform(get(entityLocation))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(movieAsString));

    }

    @Test
    void createAndUpdatePutPatchTest() throws Exception {
        MovieRequest movieRequest = new MovieRequest();
        // Create movie with errors
        movieRequest.setTitle("Inter stellar");
        movieRequest.setDescription("Interstellar is a 2014 epic science fiction film co-written, directed and " +
                "produced by Christopher Nolan.");
        movieRequest.setRating(6.8);
        movieRequest.setReleaseYear(2014);
        movieRequest.setDuration(169);

        String movieAsString = objectMapper.writeValueAsString(movieRequest);

        String entityLocation = createAndValidate(movieAsString);

        // Do PUT update, but still has mistakes
        movieRequest.setTitle("Interstelar");
        movieRequest.setRating(8.6);

        movieAsString = objectMapper.writeValueAsString(movieRequest);

        mockMvc.perform(put(entityLocation)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieAsString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(movieAsString));

        // Fixing final mistakes
        MovieRequest partialMovieRequest = new MovieRequest();
        partialMovieRequest.setTitle("Interstellar");

        movieAsString = objectMapper.writeValueAsString(partialMovieRequest);

        mockMvc.perform(patch(entityLocation)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieAsString))
                .andExpect(status().isNoContent());

        movieRequest.setTitle("Interstellar");
        movieAsString = objectMapper.writeValueAsString(movieRequest);
        // Finally check that the returned value is truly as expected
        mockMvc.perform(get(entityLocation)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(movieAsString));

    }

    @Test
    void createAndDeleteTest() throws Exception {
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setTitle("Sharknado");
        movieRequest.setDescription("When a freak hurricane swamps Los Angeles, nature's deadliest killer rules sea, " +
                "land, and air as thousands of sharks terrorize the waterlogged populace.");
        movieRequest.setRating(3.3);
        movieRequest.setReleaseYear(2013);
        movieRequest.setDuration(85);

        String movieAsString = objectMapper.writeValueAsString(movieRequest);

        String entityLocation = createAndValidate(movieAsString);

        mockMvc.perform(delete(entityLocation))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(entityLocation))
                .andExpect(status().isNotFound());
    }

    @Test
    void likeAndDisLikeTest() throws Exception {
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setTitle("Tenet");
        movieRequest.setDescription("A secret agent is given a single word as his weapon and sent to prevent the " +
                "onset of World War III");
        movieRequest.setRating(7.3);
        movieRequest.setReleaseYear(2020);
        movieRequest.setDuration(150);

        String movieAsString = objectMapper.writeValueAsString(movieRequest);

        String entityLocation = createAndValidate(movieAsString);

        mockMvc.perform(post(entityLocation + "/like"))
                .andExpect(status().isNoContent());

        mockMvc.perform(post(entityLocation + "/like"))
                .andExpect(status().isNoContent());

        mockMvc.perform(post(entityLocation + "/dislike"))
                .andExpect(status().isNoContent());

        mockMvc.perform(post(entityLocation + "/dislike"))
                .andExpect(status().isNoContent());

        mockMvc.perform(post(entityLocation + "/like"))
                .andExpect(status().isNoContent());

        MvcResult mvcResult = mockMvc.perform(get(entityLocation))
                .andExpect(status().isOk())
                .andExpect(content().json(movieAsString))
                .andReturn();

        Movie returnedMovie = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Movie.class);

        Assertions.assertEquals(3, returnedMovie.getNumLikes());
        Assertions.assertEquals(2, returnedMovie.getNumDislikes());

    }

    @Test
    void searchTitleTest() throws Exception {
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setTitle("Pirates of the Caribbean: The Curse of the Black Pearl");
        movieRequest.setDescription("Pirates 1");
        movieRequest.setRating(7.3);
        movieRequest.setReleaseYear(2003);
        movieRequest.setDuration(150);
        String movieAsString = objectMapper.writeValueAsString(movieRequest);
        createAndValidate(movieAsString);

        movieRequest = new MovieRequest();
        movieRequest.setTitle("Pirates of the Caribbean: Dead Man's Chest");
        movieRequest.setDescription("Pirates 2");
        movieRequest.setRating(7.3);
        movieRequest.setReleaseYear(2006);
        movieRequest.setDuration(150);
        movieAsString = objectMapper.writeValueAsString(movieRequest);
        createAndValidate(movieAsString);

        movieRequest = new MovieRequest();
        movieRequest.setTitle("Pirates of the Caribbean: At World's End");
        movieRequest.setDescription("Pirates 3");
        movieRequest.setRating(7.3);
        movieRequest.setReleaseYear(2007);
        movieRequest.setDuration(150);
        movieAsString = objectMapper.writeValueAsString(movieRequest);
        createAndValidate(movieAsString);

        movieRequest = new MovieRequest();
        movieRequest.setTitle("Star Wars: A New Hope");
        movieRequest.setDescription("Pirates 3");
        movieRequest.setRating(7.3);
        movieRequest.setReleaseYear(2007);
        movieRequest.setDuration(150);
        movieAsString = objectMapper.writeValueAsString(movieRequest);
        createAndValidate(movieAsString);

        // First validation tests
        mockMvc.perform(get(API_VER +"/movies/title")
                        .param("title", ""))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(API_VER +"/movies/title").param("title", "pi"))
                .andExpect(status().isBadRequest());

        MvcResult mvcResult = mockMvc.perform(get(API_VER +"/movies/title").param("title", "the"))
                .andExpect(status().isOk())
                .andReturn();

        List<Movie> movies = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Movie>>() {
        });

        Assertions.assertEquals(3, movies.size());

        movies.forEach(movie -> Assertions.assertTrue(movie.getTitle().contains("Pirates")));

        mvcResult = mockMvc.perform(get(API_VER +"/movies/title").param("title", "a new"))
                .andExpect(status().isOk())
                .andReturn();

        movies = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Movie>>() {
        });

        Assertions.assertEquals(1, movies.size());

        // test pagination
        mvcResult = mockMvc.perform(get(API_VER +"/movies/title").param("title", "the").param("page", "2").param("pageSize",
                        "2"))
                .andExpect(status().isOk())
                .andReturn();

        movies = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Movie>>() {
        });

        // 3 movies split across 2 pages
        Assertions.assertEquals(1, movies.size());

    }


    private String createAndValidate(String movieAsString) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(API_VER +"/movies")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieAsString))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().exists("Location"))
                .andExpect(content().json(movieAsString))
                .andReturn();

        return mvcResult.getResponse().getHeader("Location");


    }
}