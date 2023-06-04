package com.lawstack.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.Notification;
import com.lawstack.app.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/notification")
@Slf4j
public class NotificationController {


    @Autowired
    private NotificationService notService;


    @GetMapping("/{userId}")
    ResponseEntity<?> getAllNotifcations(@PathVariable String userId){

        List<Notification> notifications = this.notService.getAllByUserId(userId);


        if(notifications == null  || notifications.isEmpty()){
            log.error("Error in getting notifcations");
            return ResponseEntity.status(404).body(null);
        }
        
        return ResponseEntity.status(201).body(notifications);
    }

    @PostMapping("/save")
    ResponseEntity<?> saveNotification(@RequestBody Notification notification){

        Notification response = this.notService.saveNotification(notification);
    
        if(response==null){
            log.error("Error in saving notifcation");
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(201).body(notification);
    }
}
