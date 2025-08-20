package com.tiago_silveirago.course.springboot.springbootlibraryapi.validators;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.DuplicateRegistrationException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.AuthorEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorValidator {

    private final AuthorRepository repository;

    public void validate(AuthorEntity entity) {
        if (existsCreatedAuthor(entity)) {
        throw new DuplicateRegistrationException("Autor já é cadastrado");
        }
    }

    public boolean existsCreatedAuthor(AuthorEntity entity) {
        Optional<AuthorEntity> foundAuthor = repository.findByNameAndBirthdateAndNationality(
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
