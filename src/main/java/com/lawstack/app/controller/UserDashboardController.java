package com.lawstack.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.UserDashboard;
import com.lawstack.app.service.UserDashBoardService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/userdashboard")
@Slf4j
public class UserDashboardController {
    
    @Autowired
    private UserDashBoardService udService;


    @GetMapping("/{userId}")
    ResponseEntity<?> getInfo(@PathVariable String userId){

        log.info("Request to get user dashbaord info recevied");

        UserDashboard response = this.udService.getInfoByUserId(userId);

        if(response == null){

            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(201).body(response);
    }
}
