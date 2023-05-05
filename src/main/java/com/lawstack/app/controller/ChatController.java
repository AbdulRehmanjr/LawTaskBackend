package com.lawstack.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.lawstack.app.model.Chat;

@Controller
@CrossOrigin
public class ChatController {


    @Autowired
    private SimpMessagingTemplate smt;
    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    private Chat receivedMessage(@Payload Chat message){

        return message;
    }

    /**
     * @info /user/{name}/private
     */
    @MessageMapping("/private-message")
    private Chat receivedPrivateMessage(@Payload Chat message){
        smt.convertAndSendToUser(message.getReceiverName(), "/private", message);
        return message;
    }
}
