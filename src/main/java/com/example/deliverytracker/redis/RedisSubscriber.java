package com.example.deliverytracker.redis;

import com.example.deliverytracker.redis.dto.OrderCreatedEvent;
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
    // private final NotificationService notificationService; // 나중에 실제 알림을 보낼 서비스

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // 1. 받은 메시지를 OrderCreatedEvent 객체로 변환합니다.
            String publishedMessage = new String(message.getBody());
            OrderCreatedEvent event = objectMapper.readValue(publishedMessage, OrderCreatedEvent.class);

            // --- 👇 이 로그가 찍히는지 확인하는 것이 최종 목표입니다 ---
            log.info("✅ [Order Channel] 새로운 주문 수신! 가게 ID: {}, 주문 ID: {}",
                    event.getStoreId(), event.getOrderId());

            // 2. (미래의 작업) 여기서 가게 주인에게 실제 알림을 보내는 로직을 호출합니다.
            // notificationService.sendNotificationToStore(event.getStoreId(), event.getMessage());

        } catch (Exception e) {
            log.error("Redis 메시지 처리 중 에러 발생", e);
        }
    }
}