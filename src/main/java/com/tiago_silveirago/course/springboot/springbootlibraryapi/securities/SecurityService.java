package com.tiago_silveirago.course.springboot.springbootlibraryapi.securities;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.UserEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;

    public UserEntity getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String login = userDetails.getUsername();
        return userService.getByLogin(login);
    }
}
