package com.movies.services.impl;

import com.movies.exceptions.NotFoundException;
import com.movies.model.Movie;
import com.movies.model.MovieRequest;
import com.movies.repositories.MovieRepository;
import com.movies.services.MovieService;
import com.movies.util.MovieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie getMovieById(int id) {
        return movieRepository.findById(id).orElseThrow(() -> new NotFoundException("Movie with ID '" + id + "' does " +
                "not exist."));
    }

    @Override
    @Transactional
    public Movie addMovie(MovieRequest movieRequest) {
        return movieRepository.save(MovieUtil.setProperties(new Movie(), movieRequest));
    }

    @Override
    @Transactional
    public Movie updateMovieById(int id, MovieRequest movieRequest) {
        Movie movie = getMovieById(id);
        return movieRepository.save(MovieUtil.setProperties(movie, movieRequest));
    }

    @Override
    public void updateMoviePartialById(int id, MovieRequest movieRequest) {
        Movie movie = getMovieById(id);
        movieRepository.save(MovieUtil.setPropertiesPartial(movie, movieRequest));
    }

    @Override
    @Transactional
    public Movie deleteMovieById(int id) {
        Movie movie = getMovieById(id);
        movieRepository.deleteById(id);
        return movie;
    }

    @Override
    public List<Movie> findMoviesByTitle(String title, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        int limit = pageSize;
        return movieRepository.findMoviesByTitle(title, limit, offset);
    }

    @Override
    @Transactional
    public void likeMovieById(int id) {
        Movie movie = getMovieById(id);
        movie.setNumLikes(movie.getNumLikes() + 1);
        movieRepository.save(movie);
    }

    @Override
    @Transactional
    public void dislikeMovieById(int id) {
        Movie movie = getMovieById(id);
        movie.setNumDislikes(movie.getNumDislikes() + 1);
        movieRepository.save(movie);
    }

}
