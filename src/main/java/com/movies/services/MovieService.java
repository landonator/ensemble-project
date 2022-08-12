package com.movies.services;

import com.movies.model.Movie;
import com.movies.model.MovieRequest;

import java.awt.print.Pageable;
import java.util.List;

public interface MovieService {

    Movie getMovieById(int id);

    Movie addMovie(MovieRequest movieRequest);

    Movie updateMovieById(int id, MovieRequest movieRequest);

    void updateMoviePartialById(int id, MovieRequest movieRequest);

    Movie deleteMovieById(int id);

    List<Movie> findMoviesByTitle(String title, int page, int pageSize);

    void likeMovieById(int id);

    void dislikeMovieById(int id);
}
