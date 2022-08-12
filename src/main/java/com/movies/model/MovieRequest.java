package com.movies.model;

import lombok.Data;

@Data
public class MovieRequest {

    private String title;

    private String description;

    private int releaseYear;

    private int duration;

    private double rating;
}
