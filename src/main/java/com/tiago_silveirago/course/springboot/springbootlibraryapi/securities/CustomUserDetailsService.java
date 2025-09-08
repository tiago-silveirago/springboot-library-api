package com.tiago_silveirago.course.springboot.springbootlibraryapi.securities;

import com.tiago_silveirago.course.springboot.springbootlibraryapi.entities.UserEntity;
import com.tiago_silveirago.course.springboot.springbootlibraryapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService service;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity entity = service.getByLogin(login);

        if(entity == null){
            throw new UsernameNotFoundException("Usuario não encontrado!");
        }

        return User.builder()
                .username(entity.getLogin())
                .password(entity.getPassword())
                .roles(entity.getRoles().toArray(new String[entity.getRoles().size()]))
                .build();
    }
}