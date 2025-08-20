package com.tiago_silveirago.course.springboot.springbootlibraryapi.services;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.OperationNotPermittedException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.AuthorEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.AuthorRepository;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.BookRepository;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.validators.AuthorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorValidator validator;
    private final BookRepository bookRepository;

    public AuthorEntity create(AuthorEntity entity) {
        validator.validate(entity);

        return authorRepository.save(entity);
    }

    public void update(AuthorEntity entity){

        if (entity.getId()==null){
            throw new IllegalArgumentException("Para atualizar, é necessário que o autor já esteja dalvo na base de dados.");
        }

        validator.validate(entity);
        authorRepository.save(entity);
    }

    public Optional<AuthorEntity> getAuthorById(UUID id){
        return authorRepository.findById(id);
    }

    public void delete(AuthorEntity entity){

        if (thereAreLinkedBooks(entity)) {
            throw new OperationNotPermittedException("Não é permitido excluir um autor que possui livros cadastrados!");
        }

        authorRepository.delete(entity);
    }

    public List<AuthorEntity> search(String name, String nationality){

        if (name != null && nationality != null) {
            return authorRepository.findByNameAndNationality(name, nationality);
        }

        if (name != null) {
            return authorRepository.findByName(name);
        }

        if (nationality != null) {
            return authorRepository.findByNationality(nationality);
        }

        return authorRepository.findAll();
    }

    public List<AuthorEntity> searchByExample(String name, String nationality){
        AuthorEntity entity = new AuthorEntity();
        entity.setName(name);
        entity.setNationality(nationality);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<AuthorEntity> entityExample = Example.of(entity, matcher);
        return authorRepository.findAll(entityExample);
    }

    public boolean thereAreLinkedBooks(AuthorEntity entity){
        return bookRepository.existsByAuthor(entity);
    }
}
