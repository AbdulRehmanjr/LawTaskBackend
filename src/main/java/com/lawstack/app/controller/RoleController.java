package com.lawstack.app.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.Role;
import com.lawstack.app.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {
    
    @Autowired
    private RoleService roleService;
    

    @PostMapping("/create")
    public ResponseEntity<?> registerRole(@RequestBody Role role) {
        
        log.info("Creating new Role : {}",role.getRoleName());

        Role result = this.roleService.saveRole(role);

        if(result==null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Role already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Creation success");
        
    }

    @GetMapping("/all")
    public ResponseEntity<?> allRoles() {
        
        log.info("/GET : all roles");

        List<Role> roles = this.roleService.getAllRoles();
        
        if(roles == null){
            return ResponseEntity.badRequest().body("No role found.");
        }
        return ResponseEntity.ok().body(roles);
        
    }
}
