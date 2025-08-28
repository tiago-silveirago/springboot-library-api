package com.tiago_silveirago.course.springboot.springbootlibraryapi.services;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.author.AuthorRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.author.AuthorResponseDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.AuthorEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.AuthorRepository;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.BookRepository;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.validators.AuthorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.tiago_silveirago.course.springboot.springbootlibraryapi.factories.AuthorFactory.*;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorValidator validator;
    private final BookRepository bookRepository;

    public UUID create(AuthorRequestDTO request) {
        AuthorEntity entity = fromDto(request);
        validator.validate(entity);
        AuthorEntity savedEntity = authorRepository.save(entity);

        return savedEntity.getId();
    }

    public void update(String id, AuthorRequestDTO request) {
        AuthorEntity entity = validator.validateExistence(id);
        updateEntityFromDto(entity, request);
        validator.validate(entity);
        authorRepository.save(entity);
    }

    public AuthorResponseDTO getById(String id){
        AuthorEntity entity = validator.validateExistence(id);

        return fromEntity(entity);
    }

    public void delete(String id) {
        AuthorEntity entity = validator.validateExistence(id);
        validator.validateNoLinkedBooks(entity);
        authorRepository.delete(entity);
    }

    public List<AuthorResponseDTO> searchByExample(String name, String nationality) {
        AuthorEntity entity = new AuthorEntity();
        entity.setName(name);
        entity.setNationality(nationality);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<AuthorEntity> entityExample = Example.of(entity, matcher);
        List<AuthorEntity> results = authorRepository.findAll(entityExample);

        return fromEntity(results);
    }
}
