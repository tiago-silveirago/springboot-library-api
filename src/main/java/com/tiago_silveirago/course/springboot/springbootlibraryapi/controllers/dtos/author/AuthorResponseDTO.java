package com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.author;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorResponseDTO(UUID id,
                                String name,
                                LocalDate birthdate,
                                String nationality) {
}
