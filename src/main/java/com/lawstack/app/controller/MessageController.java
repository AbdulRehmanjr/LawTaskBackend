package com.lawstack.app.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.lawstack.app.model.Chat;
import com.lawstack.app.service.MessageService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MessageController {

    @Autowired
    private SimpMessagingTemplate smt;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    private Chat receivedMessage(@Payload Chat message) {

        this.messageService.saveMessages(message);

        return message;
    }

    /**
     * @info /user/{name}/private
     */
    @MessageMapping("/private-message")
    private Chat receivedPrivateMessage(@Payload Chat message) {

        try {
            this.messageService.saveMessages(message);
            message.setType("RECEIVER");

            smt.convertAndSendToUser(message.getSenderName(), "/private", message);

        } catch (Exception e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
        }

        return message;
    }

}
