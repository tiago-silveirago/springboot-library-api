package com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions;

import lombok.Getter;

public class InvalidFieldException extends RuntimeException {

    @Getter
    private String field;

    public InvalidFieldException(String message, String field) {
        super(message);
        this.field = field;
    }
}
