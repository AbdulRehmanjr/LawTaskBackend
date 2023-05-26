package com.lawstack.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.lawstack.app.model.Seller;
import com.lawstack.app.service.SellerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/seller")
@Slf4j
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @PostMapping("/save")
    ResponseEntity<?> saveSeller(@RequestBody Seller seller) {

        log.info("Received the Request for saving seller.");

        Seller response = this.sellerService.createSeller(seller);

        if (response == null) {
            log.info("Seller creation error.");
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    ResponseEntity<?> getSeller(@PathVariable String userId) {

        log.info("Received request to get seller by user Id : {}",userId);

        Seller response = this.sellerService.getSellerByUserId(userId);

        if (response == null) {
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all")
    ResponseEntity<?> getAllSellers() {

        log.info("Received request to get sellers");

        List<Seller> response = this.sellerService.getAll();

        if (response == null) {
            
            return ResponseEntity.status(401).body(null);
        }

        return ResponseEntity.status(201).body(response);
    }

}
