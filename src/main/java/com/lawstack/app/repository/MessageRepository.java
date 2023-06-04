package com.lawstack.app.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.Chat;

public interface MessageRepository extends JpaRepository<Chat,Long>{
    
    List<Chat> findAllBySenderNameAndReceiverNameOrReceiverNameAndSenderName
    (String userId, String receiverId, String receiverId2, String userId2);

    List<Chat> findAllBySenderNameAndReceiverName(String userId, String receiverId);

    List<Chat> findByReceiverNameAndSenderNameAndIsRead(String receiverName, String senderName, boolean isRead);


}
