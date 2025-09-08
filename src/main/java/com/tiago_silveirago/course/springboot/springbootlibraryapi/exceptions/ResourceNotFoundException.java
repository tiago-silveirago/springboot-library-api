package com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
