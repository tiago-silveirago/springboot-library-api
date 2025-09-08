package com.tiago_silveirago.course.springboot.springbootlibraryapi.validators;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.InvalidFieldException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.BookEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookValidator {

    public static final int PRICE_REQUIRED_YEAR = 2020;

    public void validatePriceBasedOnPublicationYear(BookEntity entity) {
        if (isRequiredFieldPriceNull(entity)) {
            throw new InvalidFieldException(
                    "price", "Para livros com ano de publicação a apartir de 2020, o preço é obrigatório.");
        }
    }

    private boolean isRequiredFieldPriceNull(BookEntity entity) {
        return entity.getPrice() == null && entity.getPublishDate().getYear() >= PRICE_REQUIRED_YEAR;
    }
}
