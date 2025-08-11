package com.tiago_silveirago.course.springboot.springbootlibraryapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private LocalDate birthdate;
    private String nationality;

    @OneToMany(mappedBy = "author")
    private List<Book> books;
}
