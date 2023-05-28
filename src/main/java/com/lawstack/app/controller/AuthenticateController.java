package com.lawstack.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.configuration.jwt.JwtUtil;
import com.lawstack.app.model.JwtRequest;
import com.lawstack.app.model.JwtResponse;
import com.lawstack.app.model.User;
import com.lawstack.app.service.UserService;
import com.lawstack.app.service.implementation.UserDetailServiceImp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/token")
public class AuthenticateController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailServiceImp userDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;


    /**
     * 
     * This function takes a JWT request and generates a JWT token using the
     * provided secret key.
     * 
     * @param jwtRequest the JWT request object containing the user's details
     * @return a JWT token containing the user's details and a signature
     * @throws Exception if there is an error while generating the JWT token
     */
    @PostMapping("/generate")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest request) {
        
        //log.info("Request for generating token. by user {}", request.getUserEmail());
        try {
            authentication(request.getUserEmail(), request.getPassword());
        } catch (Exception e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error user not found.");
        }

        UserDetails user = this.userDetailsService.loadUserByUsername(request.getUserEmail());

        String token = this.jwtUtil.generateToken(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new JwtResponse(token));

    }

    private void authentication(String username, String password) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            log.info("Authenticated User: {} ", username);
        } catch (DisabledException e) {
            log.error("USER DISABLED  {} ", e.getMessage());
            throw new Exception("USER DISABLED");
        } catch (BadCredentialsException e) {
            log.error("INVALID CREDENTIALS {} ", e.getMessage());
            throw new Exception("INVALID CREDENTIALS");
        }

    }

    @PostMapping("/current-user")
    public User getCurrentUser(@RequestBody JwtRequest request) {
        log.info("calling current user");
        User user = this.userService.getUserByEmail(request.getUserEmail());
        log.info("USER {}", user);
        return user;
    }
}
