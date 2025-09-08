package com.tiago_silveirago.course.springboot.springbootlibraryapi.services;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.authors.AuthorRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.authors.AuthorResponseDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.UserEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.DuplicateRegistrationException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.OperationNotPermittedException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.ResourceNotFoundException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.AuthorEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.AuthorRepository;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.BookRepository;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.securities.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.tiago_silveirago.course.springboot.springbootlibraryapi.factories.AuthorFactory.*;
import static com.tiago_silveirago.course.springboot.springbootlibraryapi.utils.Util.getUuidFromString;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final SecurityService securityService;

    public UUID create(AuthorRequestDTO request) {
        AuthorEntity entity = fromDto(request);
        ensureUniqueAuthor(entity);
        UserEntity user = securityService.getLoggedInUser();
        entity.setUser(user);
        AuthorEntity savedEntity = authorRepository.save(entity);

        return savedEntity.getId();
    }

    public void update(String id, AuthorRequestDTO request) {
        AuthorEntity entity = ensureAuthorExists(id);
        updateEntityFromDto(entity, request);
        ensureUniqueAuthor(entity);
        authorRepository.save(entity);
    }

    public AuthorResponseDTO getById(String id){
        AuthorEntity entity = ensureAuthorExists(id);

        return fromEntity(entity);
    }

    public void delete(String id) {
        AuthorEntity entity = ensureAuthorExists(id);
        ensureNoLinkedBooks(entity);
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

    public AuthorEntity ensureAuthorExists(String id) {
        UUID uuid = getUuidFromString(id);
        return authorRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado"));

    }

    private void ensureUniqueAuthor(AuthorEntity entity) {

        if (existsCreatedAuthor(entity)) {
            throw new DuplicateRegistrationException("Autor já é cadastrado");
        }
    }

    public void ensureNoLinkedBooks(AuthorEntity entity) {

        if (bookRepository.existsByAuthor(entity)) {
            throw new OperationNotPermittedException("Não é permitido excluir um autor que possui livros cadastrados!");
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
