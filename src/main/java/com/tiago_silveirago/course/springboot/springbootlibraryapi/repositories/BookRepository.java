package com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.AuthorEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<BookEntity, UUID> {

    boolean existsByAuthor(AuthorEntity authorEntity);
}
