package com.tiago_silveirago.course.springboot.springbootlibraryapi.services;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.author.AuthorRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.author.AuthorResponseDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.AuthorEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.AuthorRepository;
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

    private final AuthorRepository repository;
    private final AuthorValidator validator;

    public UUID create(AuthorRequestDTO request) {
        validator.validateDto(request);
        AuthorEntity entity = fromDto(request);
        validator.validate(entity);
        AuthorEntity savedEntity = repository.save(entity);

        return savedEntity.getId();
    }

    public void update(String id, AuthorRequestDTO request) {
        validator.validateDto(request);
        AuthorEntity entity = validator.validateExistence(id);
        updateEntityFromDto(entity, request);
        validator.validate(entity);
        repository.save(entity);
    }

    public AuthorResponseDTO getById(String id){
        AuthorEntity entity = validator.validateExistence(id);

        return fromEntity(entity);
    }

    public void delete(String id) {
        AuthorEntity entity = validator.validateExistence(id);
        validator.validateNoLinkedBooks(entity);
        repository.delete(entity);
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
        List<AuthorEntity> results = repository.findAll(entityExample);

        return fromEntity(results);
    }
}
