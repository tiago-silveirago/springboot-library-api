package com.tiago_silveirago.course.springboot.springbootlibraryapi.validators;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.author.AuthorRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.DuplicateRegistrationException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.OperationNotPermittedException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.ResourceNotFoundException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.AuthorEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.AuthorRepository;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.BookRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Validator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthorValidator {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final Validator validator;


    public void validateDto(AuthorRequestDTO dto) {
        Set<ConstraintViolation<AuthorRequestDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public AuthorEntity validateExistence(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return authorRepository.findById(uuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado"));
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("ID de autor inválido");
        }
    }

    public void validateNoLinkedBooks(AuthorEntity entity) {

        if (bookRepository.existsByAuthor(entity)) {
            throw new OperationNotPermittedException("Não é permitido excluir um autor que possui livros cadastrados!");
        }
    }

    public void validate(AuthorEntity entity) {

        if (existsCreatedAuthor(entity)) {
        throw new DuplicateRegistrationException("Autor já é cadastrado");
        }
    }

    public boolean existsCreatedAuthor(AuthorEntity entity) {
        Optional<AuthorEntity> foundAuthor = authorRepository.findByNameAndBirthdateAndNationality(
                entity.getName(), entity.getBirthdate(), entity.getNationality()
        );


        if (foundAuthor.isEmpty()) {
            return false;
        }


        if (entity.getId() == null) {
            return true;
        }

        return !entity.getId().equals(foundAuthor.get().getId());
    }
}
