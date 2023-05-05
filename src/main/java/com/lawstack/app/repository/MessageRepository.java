package com.lawstack.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.Message;

public interface MessageRepository extends JpaRepository<Message,Long>{
    
}
