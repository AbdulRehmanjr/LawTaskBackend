package com.lawstack.app.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Chat;
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

    @Override
    public Chat saveMessages(Chat message) {
        log.info("Saving mew Message to database");

        
        String id = UUID.randomUUID().toString();

        message.setId(id);
        return this.messageRepo.save(message);
    }

    @Override
    public List<Chat> getAllMessageByChat(String userId, String receiverId) {
        log.info("Get all chats by sender and reciver name");
        
        List<Chat> chats = this.messageRepo.findAllBySenderNameAndReceiverName(userId, receiverId);

        return chats;
    }

    
}
