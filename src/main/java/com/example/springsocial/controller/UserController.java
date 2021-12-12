package com.example.springsocial.controller;

import com.example.springsocial.api.exceptionHandling.exceptions.CustomDataNotFoundException;
import com.example.springsocial.api.films.Film;
import com.example.springsocial.api.posts.Post;
import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.model.User;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    @GetMapping("api/user/id/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("User of ID = " + id + " does not exist"));
    }
    @GetMapping("api/user")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("api/user/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomDataNotFoundException("User of ID = " + id + " does not exist");
        }
        return ResponseEntity.ok().build();
    }
}
