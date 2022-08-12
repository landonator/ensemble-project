package com.movies.util;

import com.movies.model.Movie;
import com.movies.model.MovieRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MovieUtil {
    public static Movie setProperties(Movie movie, MovieRequest movieRequest){
        movie.setTitle(movieRequest.getTitle());
        movie.setDescription(movieRequest.getDescription());
        movie.setDuration(movieRequest.getDuration());
        movie.setReleaseYear(movieRequest.getReleaseYear());
        movie.setRating(movieRequest.getRating());

        return movie;
    }

    public static Movie setPropertiesPartial(Movie movie, MovieRequest movieRequest){
        if(movieRequest.getTitle() != null){
            movie.setTitle(movieRequest.getTitle());
        }

        if(movieRequest.getDescription() != null){
            movie.setDescription(movieRequest.getDescription());
        }

        if(movieRequest.getReleaseYear() != 0) {
            movie.setReleaseYear(movieRequest.getReleaseYear());
        }

        if(movieRequest.getDuration() != 0) {
            movie.setDuration(movieRequest.getDuration());
        }

        if(movieRequest.getRating() != 0) {
            movie.setRating(movieRequest.getRating());
        }

        return movie;
    }

}
