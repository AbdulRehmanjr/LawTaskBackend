package com.lawstack.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.ChatUserList;
import com.lawstack.app.service.ChatUserListService;


import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/chatlist")
public class ChatUserListController {
    
    @Autowired
    private ChatUserListService culService;

    @PostMapping("/{userId}")
    ResponseEntity<?> makeNewList(@PathVariable String userId ){

        log.info("Adding new Chat User list");

        ChatUserList user = this.culService.addNewChatList(userId);

        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
