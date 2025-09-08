package com.tiago_silveirago.course.springboot.springbootlibraryapi.factories;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.users.UserRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.users.UserResponseDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFactory {

    public static UserEntity fromDto(UserRequestDTO request) {
        UserEntity entity = new UserEntity();
        entity.setLogin(request.login());
        entity.setPassword(request.password());
        entity.setRoles(request.roles());

        return entity;
    }

    public static UserResponseDTO fromEntity(UserEntity entity) {

        return new UserResponseDTO(
                entity.getId(),
                entity.getLogin(),
                entity.getPassword(),
                entity.getRoles());
    }

    public static Set<UserResponseDTO> fromEntity(Set<UserEntity> entities) {

        return entities
                .stream()
                .map(UserFactory::fromEntity)
                .collect(Collectors.toSet());
    }
}
