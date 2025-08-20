package com.tiago_silveirago.course.springboot.springbootlibraryapi.model;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.enums.BookGenre;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Table(name = "book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String isbn;

    private String title;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @Enumerated(EnumType.STRING)
    private BookGenre genre;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "id_author")
    private AuthorEntity author;

}
