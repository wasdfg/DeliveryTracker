package com.example.deliverytracker.mail.service;

public interface EmailService {
    void send(String to, String subject, String body);
}
