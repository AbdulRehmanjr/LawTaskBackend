package com.lawstack.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.Subscription;
import com.lawstack.app.service.SubscriptionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/subscription")
@Slf4j
public class SubscriptionController {


    @Autowired
    private SubscriptionService sService;


    @GetMapping("/info/{email}")
    ResponseEntity<?> getSubscrptionInfo(@PathVariable String email){

        log.info("Request to get the subscrption");
        Subscription sub = this.sService.getCustomerByEmail(email);

        if(sub == null){
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(201).body(sub);
    }
}
