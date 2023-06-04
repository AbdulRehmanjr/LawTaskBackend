package com.lawstack.app.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Notification;
import com.lawstack.app.repository.NotificationRepository;
import com.lawstack.app.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationServiceImp implements NotificationService{

    
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification saveNotification(Notification notification) {
        
        notification = this.notificationRepository.save(notification);

        if(notification == null){
            log.error("Error in Saving Notifaction");
            return null;
        }
        return notification;
    }

    @Override
    public List<Notification> getAllByUserId(String userId) {
        
        List<Notification> notifications = this.notificationRepository.findAllByUserId(userId);

        if(notifications ==null){
            log.error("Error in fecthing notifcations for user {}",userId);
            return null;
        }
        return notifications;
    }
    
}
