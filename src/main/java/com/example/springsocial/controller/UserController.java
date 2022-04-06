package com.example.springsocial.controller;

import com.example.springsocial.api.exceptionHandling.exceptions.CustomDataNotFoundException;
import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.model.User;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @PutMapping("api/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity updateUser(@CurrentUser UserPrincipal userPrincipal, @RequestBody User user) {
        User currentUser = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        currentUser.setName(user.getName());
        if(user.getPassword() != null){
            currentUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        final User updatedUser = userRepository.save(currentUser);

        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("api/user/{search}")
    public List<User> getUserSuggestions(@PathVariable String search) {
        List<User> allUsers = userRepository.findAll();
        List<User> filteredUsers = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getEmail().toLowerCase().contains(search.toLowerCase())
                    || user.getName().toLowerCase().contains(search.toLowerCase())) {
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
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
