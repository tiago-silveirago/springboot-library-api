package com.tiago_silveirago.course.springboot.springbootlibraryapi.validators;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.books.BookRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.DuplicateRegistrationException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.InvalidFieldException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.ResourceNotFoundException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.BookEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.BookRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookValidator {

    public static final int PRICE_REQUIRED_YEAR = 2020;

    private final BookRepository repository;
    private final Validator validator;


    public void validateDto(BookRequestDTO dto) {
        Set<ConstraintViolation<BookRequestDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public BookEntity validateExistence(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return repository.findById(uuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("ID de livro inválido");
        }
    }


    public void validate(BookEntity entity) {
        if (existBookWithIsbn(entity)) {
            throw new DuplicateRegistrationException("ISBN já cadastrado!");
        }

        if (isRequiredFieldPriceNull(entity)) {
            throw new InvalidFieldException(
                    "price", "Para livros com ano de publicação a apartir de 2020, o preço é obrigatório.");
        }
    }

    private boolean isRequiredFieldPriceNull(BookEntity entity) {
        return entity.getPrice() == null && entity.getPublishDate().getYear() >= PRICE_REQUIRED_YEAR;
    }

    private boolean existBookWithIsbn(BookEntity entity) {
        Optional<BookEntity> foundBook = repository.findByIsbn(entity.getIsbn());

        if (entity.getId() == null) {
            return foundBook.isPresent();
        }

        return foundBook
                .map(BookEntity::getId)
                .stream()
                .anyMatch(id -> !id.equals(entity.getId()));
    }
}
