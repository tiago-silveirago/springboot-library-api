package com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserRequestDTO(@NotBlank
                             String login,
                             @NotBlank
                             String password,
                             @NotNull
                             List<String> roles) {
}
