package com.lawstack.app.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawstack.app.dto.DtoConverter;
import com.lawstack.app.dto.UserDto;

import com.lawstack.app.model.User;

import com.lawstack.app.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
@CrossOrigin("${cross_origin}")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> resgisterUser(@RequestParam("file") MultipartFile profilePicture, String user) {

        log.info("/POST : saving user");

        User n_user = new User();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            n_user = objectMapper.readValue(user, User.class);
        } catch (JsonProcessingException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }

        User found = this.userService.getUserByEmail(n_user.getEmail());

        if (found != null) {
            log.error("User already exists with given Email: {}", n_user.getEmail());
            return ResponseEntity.badRequest().body("User already exists with given email");
        }

        try {
            n_user.setProfilePicture(profilePicture.getBytes());
        } catch (IOException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }

        n_user = this.userService.saveUser(n_user);
        if (n_user != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Success Fully created.");
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Error in creation user");

    }

    @GetMapping("/all/{userName}")
    public List<User> getUserByName(@PathVariable("userName") String username) {

        List<User> result = this.userService.getAllUsersByUserNameLike(username);

        if (result == null) {
            log.error("Users not found");
            return null;
        }

        log.info("User Found.");

        return result;

    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") String userId) {

        DtoConverter dtoConverter = new DtoConverter();
        User result = this.userService.getUserById(userId);

        if (result == null) {
            log.error("Users not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        log.info("User Found.");

        UserDto user = dtoConverter.UserToDto(result);

        return ResponseEntity.status(HttpStatus.FOUND).body(user);

    }

    @GetMapping("/all")
    List<User> Allusers() {
        log.info("Geting all users");
        return this.userService.getAllUsers();
    }

    // delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        this.userService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }

}