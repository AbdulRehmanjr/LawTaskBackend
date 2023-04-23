package com.lawstack.app.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawstack.app.model.Role;
import com.lawstack.app.model.User;
import com.lawstack.app.service.RoleService;
import com.lawstack.app.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/register")
    public ResponseEntity<String> resgisterUser(@RequestParam("file") MultipartFile profilePicture, String user)
            throws IOException {

        log.info("/POST : saving user");

        User n_user = new User();

        ObjectMapper objectMapper = new ObjectMapper();

        n_user = objectMapper.readValue(user, User.class);

        User found = this.userService.getUserByEmail(n_user.getEmail());

        if (found != null) {
            log.error("User already exists with given Email: {}", n_user.getEmail());
            return ResponseEntity.badRequest().body("User already exists with given email");
        }

        n_user.setProfilePicture(profilePicture.getBytes());
       
        this.userService.saveUser(n_user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Success Fully created.");

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

        User result = this.userService.getUserById(userId);

        if (result == null) {
            log.error("Users not found");
            return ResponseEntity.badRequest().body("User not found");
        }
        log.info("User Found.");

        UserDto user = new UserDto(result.getUserId(), result.getUserName(), result.getEmail(),
                result.getProfilePicture(), result.getRole());
        return ResponseEntity.status(200).body(user);

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
