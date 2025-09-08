package com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.users;

import java.util.List;
import java.util.UUID;

public record UserResponseDTO(UUID id,
                              String login,
                              String password,
                              List<String> roles) {
}
