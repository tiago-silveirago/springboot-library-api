package com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.ResponseErrorDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.author.AuthorRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.author.AuthorResponseDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.DuplicateRegistrationException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.OperationNotPermittedException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.AuthorEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.services.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.tiago_silveirago.course.springboot.springbootlibraryapi.factories.AuthorFactory.*;

@RestController
@RequestMapping("authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService service;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid AuthorRequestDTO request){
        try {
            AuthorEntity entity = fromDto(request);
            service.create(entity);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(entity.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } catch (DuplicateRegistrationException e) {
            var error = ResponseErrorDTO.conflictResponse(e.getMessage());
            return ResponseEntity.status(error.status()).body(error);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<AuthorResponseDTO> getDetails(@PathVariable("id") String id){
        Optional<AuthorEntity> optionalAuthor = ensureAuthorExists(id);

        if(optionalAuthor.isPresent()){
            return ResponseEntity.ok(fromEntity(optionalAuthor.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") String id){
       try {
           Optional<AuthorEntity> optionalAuthor = ensureAuthorExists(id);

           if (optionalAuthor.isEmpty()) {
               return ResponseEntity.notFound().build();
           }

           service.delete(optionalAuthor.get());

           return ResponseEntity.noContent().build();
       } catch (OperationNotPermittedException e) {
           var errorResponse = ResponseErrorDTO.standardResponse(e.getMessage());
           return ResponseEntity.status(errorResponse.status()).body(errorResponse);
       }
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponseDTO>> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "nationality", required = false) String nationality){
        List<AuthorEntity> list = service.searchByExample(name, nationality);
        List<AuthorResponseDTO> responses = fromEntity(list);

        return ResponseEntity.ok(responses);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody @Valid AuthorRequestDTO request){
       try {
           Optional<AuthorEntity> optionalAuthor = ensureAuthorExists(id);

           if (optionalAuthor.isEmpty()) {
               return ResponseEntity.notFound().build();
           }

           AuthorEntity entity = optionalAuthor.get();
           updateEntityFromDto(entity, request);
           service.update(entity);

           return ResponseEntity.noContent().build();
       } catch (DuplicateRegistrationException e) {
           var error = ResponseErrorDTO.conflictResponse(e.getMessage());
           return ResponseEntity.status(error.status()).body(error);
       }
    }


    private Optional<AuthorEntity> ensureAuthorExists(String id) {
        try {
            return service.getAuthorById(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
