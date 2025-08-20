package com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.common;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.FieldErrorDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers.dtos.ResponseErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseErrorDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<FieldErrorDTO> errorsList = fieldErrors
                .stream()
                .map(fieldError -> new FieldErrorDTO(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()))
                .toList();
        return new ResponseErrorDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação",  errorsList);
    }
}
