package com.lawstack.app.service;

import java.util.List;

import com.lawstack.app.model.Chat;
import com.lawstack.app.model.Message;

public interface MessageService {

    Message sendMessage(Message message,String sendTo);

    Message receiveMessage(String sendFrom,String sendTo);

    Chat saveMessages(Chat message);

    List<Chat> getAllMessageByChat(String userId,String receiverId);

    Boolean readOneMessage(Chat message);

    Boolean readAllMessages(String from,String to);


}
