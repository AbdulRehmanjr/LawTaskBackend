package com.lawstack.app.service;

import com.lawstack.app.model.Message;

public interface MessageService {

    Message sendMessage(Message message,String sendTo);

    Message receiveMessage(String sendFrom,String sendTo);

}
