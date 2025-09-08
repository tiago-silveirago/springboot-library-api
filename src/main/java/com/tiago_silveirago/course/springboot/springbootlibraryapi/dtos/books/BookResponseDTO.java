package com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.books;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.authors.AuthorResponseDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.enums.BookGenre;

import java.time.LocalDate;
import java.util.UUID;

public record BookResponseDTO(UUID id,
                              String isbn,
                              String title,
                              LocalDate publishDate,
                              BookGenre genre,
                              Double price,
                              AuthorResponseDTO author) {
}
