package com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AuthorRequestDTO(@NotBlank(message = "campo obrigatório")
                               @Size(min = 2, max = 100, message = "campo fora do tamanho padrão")
                               String name,
                               @NotNull(message = "campo obrigatório")
                               @Past
                               LocalDate birthdate,
                               @NotBlank(message = "campo obrigatório")
                               @Size(min = 2, max = 50, message = "campo fora do tamanho padrão")
                               String nationality) {
}
