package com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.handlers;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.errors.FieldErrorDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.errors.ResponseErrorDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.DuplicateRegistrationException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.InvalidFieldException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.OperationNotPermittedException;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseErrorDTO handleResourceNotFound(ResourceNotFoundException e) {
        return ResponseErrorDTO.standardResponse(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorDTO handleIllegalArgument(IllegalArgumentException e) {
        return ResponseErrorDTO.standardResponse("ID inválido.");
    }

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

        return new ResponseErrorDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação",
                errorsList);
    }

    @ExceptionHandler(DuplicateRegistrationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseErrorDTO handleDuplicateRegistrationException(DuplicateRegistrationException e) {

        return ResponseErrorDTO.conflictResponse(e.getMessage());
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorDTO handleOperationNotPermittedException(OperationNotPermittedException e) {
        return ResponseErrorDTO.standardResponse(e.getMessage());
    }

    @ExceptionHandler(InvalidFieldException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseErrorDTO handleInvalidFieldException(InvalidFieldException e) {
        return new ResponseErrorDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação.",
                List.of(new FieldErrorDTO(e.getField(), e.getMessage())));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseErrorDTO handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseErrorDTO(HttpStatus.FORBIDDEN.value(), "Acesso negado.", List.of());

    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseErrorDTO unhandledErrors(RuntimeException e) {
        System.out.println(e.getMessage());

        return new ResponseErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro inesperado. entre em contato com a administração.",
                List.of());
    }
}
