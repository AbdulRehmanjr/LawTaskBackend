package com.lawstack.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.SellerJoin;
import com.lawstack.app.model.UserJoin;
import com.lawstack.app.service.SellerAndUserJoinService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/join")
public class JoinController {
    
    @Autowired
    private SellerAndUserJoinService saujService;


    @GetMapping("/users")
    ResponseEntity<?> getAllUsers(){

        List<UserJoin> userJoins = this.saujService.getAllUsers();


        if(userJoins==null){
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(201).body(userJoins);
    }
    

    @GetMapping("/sellers")
    ResponseEntity<?> getAllSellers(){

        List<SellerJoin> sellerJoins = this.saujService.getAllSellers();


        if(sellerJoins==null){
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(201).body(sellerJoins);
    }
}
