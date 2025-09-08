package com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.books.BookRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.books.BookResponseDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.enums.BookGenre;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController implements GenericController{

    private final BookService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<Void> create(@RequestBody @Valid BookRequestDTO request) {
        UUID id = service.create(request);
        URI location = generateHeaderLocation(id);

        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<BookResponseDTO> getDetails(@PathVariable("id") String id){
        BookResponseDTO response = service.getById(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody @Valid BookRequestDTO request) {
        service.update(id, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<Page<BookResponseDTO>> search(
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "name-author", required = false) String nameAuthor,
            @RequestParam(value = "genre", required = false) BookGenre genre,
            @RequestParam(value = "publish-year", required = false) Integer publishYear,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "10")  Integer pageSize) {
        Page<BookResponseDTO> responses = service.search(isbn, title, nameAuthor, genre, publishYear, page, pageSize);

        return ResponseEntity.ok(responses);
    }
}
