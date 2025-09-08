package com.tiago_silveirago.course.springboot.springbootlibraryapi.utils;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.ResourceNotFoundException;

import java.util.UUID;

public class Util {

    public static UUID getUuidFromString(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("ID inválido");
        }
    }
}
