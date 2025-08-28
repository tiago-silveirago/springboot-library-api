package com.tiago_silveirago.course.springboot.springbootlibraryapi.repositories.specs;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.BookEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.model.enums.BookGenre;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecs {

    public static Specification<BookEntity> isbnEqual(String isbn) {
        return (root,criteriaQuery,criteriaBuilder)
                -> criteriaBuilder.equal(root.get("isbn"), isbn);
    }

    public static Specification<BookEntity> titleLike(String title) {
        return (root,criteriaQuery,criteriaBuilder)
                -> criteriaBuilder.equal(criteriaBuilder.upper(root.get("title")), "%" + title.toUpperCase() + "%");
    }

    public static Specification<BookEntity> authorNameLike(String name) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<Object, Object> auhorJoin = root.join("author", JoinType.INNER);

            return criteriaBuilder.like(
                    criteriaBuilder.upper(auhorJoin.get("name")),
                    "%" + name.toUpperCase() + "%");
        };
    }

    public static Specification<BookEntity> genreEqual(BookGenre genre) {
        return ((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("genre"), genre));
    }

    public static Specification<BookEntity> publishYearEqual(Integer publishYear) {
        return ((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder
                .equal(criteriaBuilder.function(
                                "to_char",
                                String.class,
                                root.get("publishDate"),
                                criteriaBuilder.literal("YYYY")),
                        publishYear.toString()));
    }
}
