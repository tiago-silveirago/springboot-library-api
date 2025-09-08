package com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.authors.AuthorRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.authors.AuthorResponseDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.services.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController implements GenericController {

    private final AuthorService service;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> create(@RequestBody @Valid AuthorRequestDTO request) {

        UUID id = service.create(request);
        URI location = generateHeaderLocation(id);


        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody @Valid AuthorRequestDTO request) {
        service.update(id, request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<AuthorResponseDTO> getDetails(@PathVariable("id") String id) {
        AuthorResponseDTO response = service.getById(id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<List<AuthorResponseDTO>> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "nationality", required = false) String nationality) {
        List<AuthorResponseDTO> responses = service.searchByExample(name, nationality);

        return ResponseEntity.ok(responses);
    }
}
