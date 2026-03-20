package com.example.deliverytracker.notification.service;

import com.example.deliverytracker.config.SseConfig;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.entity.OrderStatusChangedEvent;
import com.example.deliverytracker.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SseConfig.SseEmitters sseEmitters;

    private final RedisPublisher redisPublisher;

    public SseEmitter subscribe(Long storeId) {
        SseEmitter emitter = new SseEmitter(60 * 1000L * 60);
        return sseEmitters.add(storeId, emitter);
    }

    public void sendNewOrderNotification(Long storeId, Long orderId) {
        String message = "새 주문이 접수되었습니다! (주문번호: " + orderId + ")";
        sseEmitters.sendToStore(storeId, "newOrder", message);
    }

    public void notifyOrderStatusChanged(Long orderId, Long userId, Order.Status status) {

        OrderStatusChangedEvent event = new OrderStatusChangedEvent(orderId, userId, status);

        redisPublisher.publish("order-channel", event);
    }
}
