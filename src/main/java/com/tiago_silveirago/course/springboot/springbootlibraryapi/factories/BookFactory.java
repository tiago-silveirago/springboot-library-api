package com.tiago_silveirago.course.springboot.springbootlibraryapi.factories;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.author.AuthorRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.books.BookRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.books.BookResponseDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.AuthorEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.BookEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public class BookFactory {

    private BookFactory() {
    }

    public static BookEntity fromDto(BookRequestDTO request){
        AuthorEntity author = new AuthorEntity();
        author.setId(request.idAuthor());

        BookEntity book = new BookEntity();
        book.setIsbn(request.isbn());
        book.setTitle(request.title());
        book.setPublishDate(request.publishDate());
        book.setGenre(request.genre());
        book.setPrice(request.price());
        book.setAuthor(author);

        return book;
    }

    public static BookResponseDTO fromEntity(BookEntity entity) {
        return new BookResponseDTO(
                entity.getId(),
                entity.getIsbn(),
                entity.getTitle(),
                entity.getPublishDate(),
                entity.getGenre(),
                entity.getPrice(),
                AuthorFactory.fromEntity(entity.getAuthor()));
    }

    public static Page<BookResponseDTO> fromEntity(Page<BookEntity> entities) {

        return entities.map(BookFactory::fromEntity);
    }

    public static void updateEntityFromDto(BookEntity entity, BookRequestDTO request, AuthorEntity author) {
        entity.setIsbn(request.isbn());
        entity.setTitle(request.title());
        entity.setPublishDate(request.publishDate());
        entity.setGenre(request.genre());
        entity.setPrice(request.price());
        entity.setAuthor(author);
    }
}
