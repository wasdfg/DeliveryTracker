package com.example.deliverytracker.redis;

import com.example.deliverytracker.order.entity.OrderCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    // private final NotificationService notificationService; // 예: 알림 서비스 주입
    // private final DeliveryService deliveryService;       // 예: 배달 서비스 주입

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {

            String publishedMessage = new String(message.getBody());
            OrderCreatedEvent event = objectMapper.readValue(publishedMessage, OrderCreatedEvent.class);
            log.info("Received message from order-channel: orderId={}, userId={}", event.getOrderId(), event.getUserId());

            // 여기에 실제 후속 처리 로직을 호출합니다.
            // 예: notificationService.sendNotificationToStoreOwner(event.getOrderId());
            // 예: deliveryService.requestDeliveryDispatch(event.getOrderId());

        } catch (Exception e) {
            log.error("Error processing message from Redis", e);
        }
    }
}