package com.tiago_silveirago.course.springboot.springbootlibraryapi.factories;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.authors.AuthorRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.authors.AuthorResponseDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.AuthorEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorFactory {

    public static AuthorEntity fromDto(AuthorRequestDTO request) {
        AuthorEntity entity = new AuthorEntity();
        updateEntityFromDto(entity, request);

        return entity;
    }

    public static AuthorResponseDTO fromEntity(AuthorEntity entity) {
        return new AuthorResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getBirthdate(),
                entity.getNationality());
    }

    public static List<AuthorResponseDTO> fromEntity(List<AuthorEntity> entities) {
        return entities
                .stream()
                .map(AuthorFactory::fromEntity)
                .toList();
    }


    public static void updateEntityFromDto(AuthorEntity entity, AuthorRequestDTO request) {
        entity.setName(request.name());
        entity.setBirthdate(request.birthdate());
        entity.setNationality(request.nationality());
    }
}
