package com.tiago_silveirago.course.springboot.springbootlibraryapi.controllers;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.dtos.users.UserRequestDTO;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController implements GenericController {

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody UserRequestDTO request) {
        service.create(request);
    }
}
