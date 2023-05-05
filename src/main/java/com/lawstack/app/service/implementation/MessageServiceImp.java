package com.lawstack.app.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Message;
import com.lawstack.app.repository.MessageRepository;
import com.lawstack.app.service.MessageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageServiceImp  implements MessageService{

    @Autowired
    private MessageRepository messageRepo;

    @Override
    public Message sendMessage(Message message, String sendTo) {
       
        return message;
    }

    @Override
    public Message receiveMessage(String sendFrom, String sendTo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'receiveMessage'");
    }

    
}
