package com.lawstack.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.Chat;
import com.lawstack.app.model.ChatUserList;
import com.lawstack.app.model.User;
import com.lawstack.app.service.ChatUserListService;
import com.lawstack.app.service.MessageService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/chatlist")
public class ChatUserListController {
    
    @Autowired
    private ChatUserListService culService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/{userId}")
    ResponseEntity<?> makeNewList(@PathVariable String userId ){

        log.info("Adding new Chat User list");

        ChatUserList user = this.culService.addNewChatList(userId);

        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/{userId}/{receiverId}")
    ResponseEntity<?> addUserToChat(@PathVariable String userId ,@PathVariable String receiverId){

        log.info("Adding new User to chat");

        ChatUserList user = this.culService.addUserToChatList(userId,receiverId);

        if(user==null){
            return ResponseEntity.status(401).body(null);
        }
        return ResponseEntity.status(201).body(user);
    }
    
    @GetMapping("/{userId}")
    ResponseEntity<?> getChatListByUserId(@PathVariable String userId ){

        log.info("Getting chat list by userId");

        List<User> users = this.culService.getChatListById(userId);

        if(users.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    @GetMapping("/{userId}/{receiverId}")
    ResponseEntity<?> getAllChatByRoom(@PathVariable String userId ,@PathVariable String receiverId){

        log.info("Getting all chats of room");

        List<Chat> chats = this.messageService.getAllMessageByChat(userId,receiverId);

        if(chats==null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
        return ResponseEntity.status(201).body(chats);
    }
}
