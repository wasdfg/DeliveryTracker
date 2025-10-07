package com.example.deliverytracker.redis;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Notification;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendNotification(String fcmToken, String title, String body) {
        if (fcmToken == null || fcmToken.isEmpty()) {
            return; // 토큰이 없으면 보내지 않음
        }

        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            // 로깅 및 예외 처리
        }
    }
}
