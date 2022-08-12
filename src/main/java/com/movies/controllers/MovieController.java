package com.movies.controllers;

import com.google.common.base.Preconditions;
import com.movies.model.Movie;
import com.movies.model.MovieRequest;
import com.movies.services.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

/**
 * Handles all the REST operations for Movies in particular the CRUD based operations.
 */
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class MovieController extends BaseController {

    private static final String MOVIES_URL_PREFIX = "/movies";

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(path = MOVIES_URL_PREFIX + "/{movieId}")
    public ResponseEntity<Movie> getMovieById(@PathVariable("movieId") int movieId) {
        return ResponseEntity.ok(movieService.getMovieById(movieId));
    }

    @PostMapping(path = MOVIES_URL_PREFIX)
    public ResponseEntity<Movie> addMovie(@RequestBody MovieRequest movieRequest) throws URISyntaxException {
        log.info("Adding movie.");
        validateMovieRequest(movieRequest);
        Movie movie = movieService.addMovie(movieRequest);
        URI movieUri = new URI("/api/v1/" + MOVIES_URL_PREFIX + "/" + movie.getId());
        return ResponseEntity.created(movieUri).body(movie);
    }

    @PutMapping(path = MOVIES_URL_PREFIX + "/{movieId}")
    public ResponseEntity<Movie> updateMovie(@PathVariable("movieId") int movieId,
                                             @RequestBody MovieRequest movieRequest) {
        log.info("Getting movie with ID '{}'.", movieId);
        validateMovieRequest(movieRequest);
        return ResponseEntity.ok(movieService.updateMovieById(movieId, movieRequest));
    }

    @PatchMapping(path = MOVIES_URL_PREFIX + "/{movieId}")
    public ResponseEntity<Void> updateMoviePartial(@PathVariable("movieId") int movieId,
                                                   @RequestBody MovieRequest movieRequest) {
        log.info("Getting movie with ID '{}'.", movieId);
        validateMovieRequestPartial(movieRequest);
        movieService.updateMoviePartialById(movieId, movieRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = MOVIES_URL_PREFIX + "/title")
    public ResponseEntity<List<Movie>> getMoviesByTitle(@RequestParam(name = "title") String title,
                                                        @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                        @RequestParam(name = "pageSize", required = false, defaultValue = "25") int pageSize) {
        if (title.isBlank()) {
            throw new IllegalArgumentException("Search query cannot be empty.");
        } else if (title.length() < 3) {
            throw new IllegalArgumentException("Search query must be at least 3 letters in length.");
        }

        if (page < 1) {
            throw new IllegalArgumentException("The requested page needs to be greater than 1.");
        }

        if (pageSize < 2) {
            throw new IllegalArgumentException("The page size needs to be greater than 2.");
        }

        log.info("Searching for movies that contain '{}' in title.", title);
        return ResponseEntity.ok(movieService.findMoviesByTitle(title, page, pageSize));
    }

    @DeleteMapping(path = MOVIES_URL_PREFIX + "/{movieId}")
    public ResponseEntity<Void> deleteMovieById(@PathVariable("movieId") int movieId) {
        log.info("Deleting movie with ID '{}'", movieId);
        movieService.deleteMovieById(movieId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = MOVIES_URL_PREFIX + "/{movieId}/like")
    public ResponseEntity<Void> likeMovieById(@PathVariable("movieId") int movieId) {
        movieService.likeMovieById(movieId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = MOVIES_URL_PREFIX + "/{movieId}/dislike")
    public ResponseEntity<List<Movie>> disLikeMovieById(@PathVariable("movieId") int movieId) {
        movieService.dislikeMovieById(movieId);
        return ResponseEntity.noContent().build();
    }

    private void validateMovieRequest(MovieRequest movieRequest) {
        Preconditions.checkArgument(movieRequest.getTitle() != null && !movieRequest.getTitle().isBlank(), "Movie " +
                "title cannot be empty");
        Preconditions.checkArgument(movieRequest.getReleaseYear() > 1870, "Invalid release year, must be greater than" +
                " 1870");
        Preconditions.checkArgument(movieRequest.getRating() >= 1 && movieRequest.getRating() <= 10, "Rating must" +
                " be between 1 and 10 (inclusive).");
        Preconditions.checkArgument(movieRequest.getDescription() != null && !movieRequest.getDescription().isBlank()
                , "Movie description cannot be empty.");
        Preconditions.checkArgument(movieRequest.getDuration() >= 1, "Duration needs to be more than 0 minutes.");

    }

    private void validateMovieRequestPartial(MovieRequest movieRequest) {
        Preconditions.checkArgument(movieRequest.getTitle() != null
                        || movieRequest.getReleaseYear() != 0
                        || movieRequest.getRating() != 0
                        || movieRequest.getDescription() != null
                        || movieRequest.getDuration() != 0,
                "No appropriate values set to update the resource.");

        if (movieRequest.getTitle() != null) {
            Preconditions.checkArgument(!movieRequest.getTitle().isBlank(), "Movie title cannot be empty");
        }

        if (movieRequest.getReleaseYear() != 0) {
            Preconditions.checkArgument(movieRequest.getReleaseYear() > 1870, "Invalid release year, must be greater " +
                    "than 1870");
        }

        if (movieRequest.getRating() != 0) {
            Preconditions.checkArgument(movieRequest.getRating() >= 1 && movieRequest.getRating() <= 10, "Rating must" +
                    " be between 1 and 10 (inclusive).");
        }

        if (movieRequest.getDescription() != null) {
            Preconditions.checkArgument(!movieRequest.getDescription().isBlank()
                    , "Movie description cannot be empty.");
        }

        if (movieRequest.getDuration() != 0) {
            Preconditions.checkArgument(movieRequest.getDuration() >= 1, "Duration needs to be more than 0 minutes.");

        }

    }
}
