package com.lawstack.app.service;



public interface EmailService {

    void sendMail(String to, String subject, String text);
}
