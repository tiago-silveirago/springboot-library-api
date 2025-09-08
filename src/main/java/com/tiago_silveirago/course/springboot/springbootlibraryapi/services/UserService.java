package com.tiago_silveirago.course.springboot.springbootlibraryapi.services;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.users.UserRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.UserEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.tiago_silveirago.course.springboot.springbootlibraryapi.factories.UserFactory.fromDto;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UUID create(UserRequestDTO request) {
        UserEntity entity = fromDto(request);
        entity.setPassword(encoder.encode(entity.getPassword()));
        UserEntity savedEntity = userRepository.save(entity);

        return savedEntity.getId();
    }

    public UserEntity getByLogin(String login) {
        return userRepository.findByLogin(login);
    }

}
