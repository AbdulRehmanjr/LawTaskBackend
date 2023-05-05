package com.lawstack.app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.Message;
import com.lawstack.app.service.MessageService;

import io.micrometer.core.ipc.http.HttpSender.Response;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/message")
@RestController
@Slf4j
public class MessageController {
    
    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    ResponseEntity<?> sendMessage(@RequestBody Message message){

        log.info("MEssage :",message);
        return ResponseEntity.status(HttpStatus.CREATED).body("found");
    }
}
