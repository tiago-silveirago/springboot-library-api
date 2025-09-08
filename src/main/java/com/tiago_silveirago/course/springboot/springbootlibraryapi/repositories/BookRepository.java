package com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.AuthorEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<BookEntity, UUID>, JpaSpecificationExecutor<BookEntity> {

    boolean existsByAuthor(AuthorEntity authorEntity);

    Optional<BookEntity> findByIsbn(String isbn);
}
