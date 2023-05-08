package com.lawstack.app.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.Chat;

public interface MessageRepository extends JpaRepository<Chat,Long>{
    
    List<Chat> findAllBySenderNameAndReceiverName(String userId,String receiverId);
}
