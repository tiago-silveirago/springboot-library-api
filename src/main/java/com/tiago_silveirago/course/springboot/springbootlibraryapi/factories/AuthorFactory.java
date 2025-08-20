package com.tiago_silveirago.course.springboot.springbootlibraryapi.factories;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.author.AuthorRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.author.AuthorResponseDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.AuthorEntity;

import java.util.List;

public class AuthorFactory {

    private AuthorFactory() {
    }

    public static AuthorEntity fromDto(AuthorRequestDTO request){
        AuthorEntity author = new AuthorEntity();
        author.setName(request.name());
        author.setBirthdate(request.birthdate());
        author.setNationality(request.nationality());

        return author;
    }

    public static AuthorResponseDTO fromEntity(AuthorEntity entity){

        return new AuthorResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getBirthdate(),
                entity.getNationality());
    }

    public static List<AuthorResponseDTO> fromEntity(List<AuthorEntity> entities){

        return entities.stream()
                .map(AuthorFactory::fromEntity)
                .toList();
    }


    public static void updateEntityFromDto(AuthorEntity entity, AuthorRequestDTO request) {
        entity.setName(request.name());
        entity.setBirthdate(request.birthdate());
        entity.setNationality(request.nationality());
    }
}
