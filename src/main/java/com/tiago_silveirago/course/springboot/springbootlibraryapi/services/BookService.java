package com.tiago_silveirago.course.springboot.springbootlibraryapi.services;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.books.BookRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.books.BookResponseDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.AuthorEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.BookEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.enums.BookGenre;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.BookRepository;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.validators.AuthorValidator;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.validators.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.tiago_silveirago.course.springboot.springbootlibraryapi.factories.BookFactory.*;
import static com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.specs.BookSpecs.*;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final BookValidator validator;
    private final AuthorValidator authorValidator;


    public UUID create(BookRequestDTO request) {
        validator.validateDto(request);
        BookEntity entity = fromDto(request);
        validator.validate(entity);
        BookEntity saved = repository.save(entity);

        return saved.getId();
    }

    public BookResponseDTO getById(String id) {
        BookEntity entity = validator.validateExistence(id);

        return fromEntity(entity);
    }

    public void update(String id, BookRequestDTO request) {
        validator.validateDto(request);
        BookEntity entity = validator.validateExistence(id);
        AuthorEntity author = authorValidator.validateExistence(request.idAuthor().toString());
        updateEntityFromDto(entity, request, author);
        validator.validate(entity);
        repository.save(entity);
    }

    public void delete(String id) {
        BookEntity entity = validator.validateExistence(id);
        repository.delete(entity);
    }

    public Page<BookResponseDTO> search(
            String isbn,
            String title,
            String authorName,
            BookGenre genre,
            Integer publicationYear,
            Integer page,
            Integer size) {
        Specification<BookEntity> specifications =
                (root, criteriaQuery, criteriaBuilder)
                        -> criteriaBuilder.conjunction();

        if (isbn != null) {
            specifications = specifications.and(isbnEqual(isbn));
        }

        if (title != null) {
            specifications = specifications.and(titleLike(title));
        }

        if (authorName != null) {
            specifications = specifications.and(authorNameLike(authorName));
        }

        if (genre != null) {
            specifications = specifications.and(genreEqual(genre));
        }

        if (publicationYear != null) {
            specifications = specifications.and(publishYearEqual(publicationYear));
        }

        Pageable pageRequest = PageRequest.of(page, size);
        Page<BookEntity> result = repository.findAll(specifications, pageRequest);

        return fromEntity(result);
    }
}
