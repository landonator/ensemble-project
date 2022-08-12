package com.movies.model;

import lombok.Data;

@Data
public class Error {

    private final String message;
    private final int statusCode;
}
