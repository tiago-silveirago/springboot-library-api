package com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.books;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.enums.BookGenre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.time.LocalDate;
import java.util.UUID;

public record BookRequestDTO(@NotBlank(message = "campo obrigatório")
                             @ISBN
                             String isbn,
                             @NotBlank(message = "campo obrigatório")
                             String title,
                             @NotNull(message = "campo obrigatório")
                             @Past(message = "não pode ser uma data futura")
                             LocalDate publishDate,
                             BookGenre genre,
                             Double price,
                             @NotNull(message = "campo obrigatório")
                             UUID idAuthor) {
}
