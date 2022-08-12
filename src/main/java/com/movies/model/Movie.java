package com.movies.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents a Movie.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "MOVIES")
public class Movie {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String title;

    private String description;

    private Integer releaseYear;

    private Integer duration;

    private Double rating;

    private int numLikes;

    private int numDislikes;
}
