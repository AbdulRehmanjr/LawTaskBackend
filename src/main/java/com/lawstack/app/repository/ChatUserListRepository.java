package com.lawstack.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.ChatUserList;

public interface ChatUserListRepository extends JpaRepository<ChatUserList,String>{
    
    ChatUserList findByUserId(String userId);
}
