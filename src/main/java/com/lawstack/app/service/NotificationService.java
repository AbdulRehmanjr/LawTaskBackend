package com.lawstack.app.service;

import java.util.List;

import com.lawstack.app.model.Notification;

public interface NotificationService {

    Notification saveNotification(Notification notification);

    List<Notification> getAllByUserId(String userId);
}
