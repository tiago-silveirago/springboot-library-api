package com.tiago_silveirago.course.springboot.springbootlibraryapi.services;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.books.BookRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.books.BookResponseDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.UserEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.DuplicateRegistrationException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.ResourceNotFoundException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.AuthorEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.BookEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.enums.BookGenre;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.BookRepository;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.securities.SecurityService;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.validators.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.tiago_silveirago.course.springboot.springbootlibraryapi.factories.BookFactory.*;
import static com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.specifications.BookSpecification.*;
import static com.tiago_silveirago.course.springboot.springbootlibraryapi.utils.Util.getUuidFromString;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final AuthorService authorService;
    private final BookValidator validator;
    private final SecurityService securityService;


    public UUID create(BookRequestDTO request) {
        BookEntity entity = fromDto(request);
        ensureUniqueIsbn(entity);
        UserEntity user = securityService.getLoggedInUser();
        entity.setUser(user);
        validator.validatePriceBasedOnPublicationYear(entity);
        BookEntity saved = repository.save(entity);

        return saved.getId();
    }

    public void update(String id, BookRequestDTO request) {
        BookEntity entity = ensureBookExists(id);
        AuthorEntity author = authorService.ensureAuthorExists(request.idAuthor().toString());
        updateEntityFromDto(entity, request, author);
        ensureUniqueIsbn(entity);
        validator.validatePriceBasedOnPublicationYear(entity);
        repository.save(entity);
    }

    public BookResponseDTO getById(String id) {
        BookEntity entity = ensureBookExists(id);

        return fromEntity(entity);
    }

    public void delete(String id) {
        BookEntity entity = ensureBookExists(id);
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

    public BookEntity ensureBookExists(String id) {
        UUID uuid = getUuidFromString(id);
        return repository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));
    }

    private void ensureUniqueIsbn(BookEntity entity) {
        if (existBookWithIsbn(entity)) {
            throw new DuplicateRegistrationException("ISBN já cadastrado!");
        }
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
