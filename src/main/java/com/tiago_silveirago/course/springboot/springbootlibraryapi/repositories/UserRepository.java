package com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

UserEntity findByLogin(String login);
}
