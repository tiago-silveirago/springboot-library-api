package com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions;

public class OperationNotPermittedException extends RuntimeException {
    public OperationNotPermittedException(String message) {
        super(message);
    }
}
