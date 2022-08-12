package com.movies.repositories;

import com.movies.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {

    boolean existsById(int id);

    @Query(value = "SELECT * FROM movies WHERE movies.title LIKE %:title% LIMIT :limit OFFSET :offset", nativeQuery =
            true)
        List<Movie> findMoviesByTitle(@Param("title") String title, @Param("limit") int limit,
                                      @Param("offset") int offset);
}
