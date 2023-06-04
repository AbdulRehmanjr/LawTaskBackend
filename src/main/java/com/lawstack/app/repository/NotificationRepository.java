package com.lawstack.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Integer>{
    
    List<Notification> findAllByUserId(String userId);
}
