package com.movies.controllers;

import com.movies.exceptions.NotFoundException;
import com.movies.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseController {

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(NotFoundException nfe) {
        return new Error(nfe.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Error handleIllegalArgumentException(IllegalArgumentException iae) {
        return new Error(iae.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

}
