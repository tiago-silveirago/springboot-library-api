package com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<AuthorEntity, UUID> {

    List<AuthorEntity> findByName(String name);
    List<AuthorEntity> findByNationality(String nationality);
    List<AuthorEntity> findByNameAndNationality(String name, String nationality);

    Optional<AuthorEntity> findByNameAndBirthdateAndNationality(String name, LocalDate birthdate, String nationality);
}
