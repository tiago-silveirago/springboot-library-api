package com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions;

public class DuplicateRegistrationException extends RuntimeException {

    public DuplicateRegistrationException(String message) {
        super(message);
    }
}
