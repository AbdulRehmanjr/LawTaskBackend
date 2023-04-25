package com.lawstack.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lawstack.app.model.Seller;
import com.lawstack.app.service.SellerService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/seller")
@CrossOrigin("${cross_origin}")
public class SellerController {
    
    @Autowired
    private SellerService sellerService;


    /**
    @apiNote This api will forward request to service layer 
    @param User,picture,document  These are recieved as formData
    @since v1.0.0
     */
    @PostMapping("/request")
    ResponseEntity<?> requestSeller(String user,@RequestParam("picture") MultipartFile profilePictre, @RequestParam("document") MultipartFile document){
        log.info("/POST : Request for seller account");

        Seller seller = this.sellerService.requestForSeller(user, profilePictre, document);

        if(seller ==null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Error in saving the seller provided data.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Creation Successfull Now Please wait for admin review on Request");
    }
}
