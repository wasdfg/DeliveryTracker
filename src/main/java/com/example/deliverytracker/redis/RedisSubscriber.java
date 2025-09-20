package com.example.deliverytracker.redis;

import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.entity.OrderStatusChangedEvent;
import com.example.deliverytracker.redis.dto.DeliveryStartedEvent;
import com.example.deliverytracker.redis.dto.OrderAcceptedEvent;
import com.example.deliverytracker.redis.dto.OrderCreatedEvent;
import com.fasterxml.jackson.databind.JsonNode;
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
            // 1. 먼저 메시지를 JsonNode로 읽어 어떤 필드가 있는지 확인
            JsonNode jsonNode = objectMapper.readTree(publishedMessage);

            // 2. 메시지에 포함된 필드를 보고 어떤 이벤트인지 추론
            if (jsonNode.has("storeId")) { // 'storeId'가 있으면 OrderCreatedEvent
                OrderCreatedEvent event = objectMapper.treeToValue(jsonNode, OrderCreatedEvent.class);
                log.info("✅ [주문 생성] 가게 ID: {}, 주문 ID: {}", event.getStoreId(), event.getOrderId());
                // notificationService.sendToStore(event);

            } else if (jsonNode.has("newStatus")) { // 'newStatus'가 있으면 OrderStatusChangedEvent
                // --- 👇 이 부분을 추가하세요 ---
                OrderStatusChangedEvent event = objectMapper.treeToValue(jsonNode, OrderStatusChangedEvent.class);

                // 상태에 따라 다른 메시지 생성
                String notificationMessage = createMessageForStatus(event.getNewStatus());

                log.info("✅ [상태 변경] 사용자 ID: {}, 메시지: {}", event.getUserId(), notificationMessage);
                // notificationService.sendToUser(event.getUserId(), notificationMessage);

            }else if (jsonNode.has("riderName")) { // 'riderName'이 있으면 DeliveryStartedEvent
                DeliveryStartedEvent event = objectMapper.treeToValue(jsonNode, DeliveryStartedEvent.class);
                log.info("✅ [배달 시작] 사용자 ID: {}, 라이더: {}", event.getUserId(), event.getRiderName());
                // notificationService.sendToUser(event);

            } else if (jsonNode.has("userId")) { // 'userId'만 있으면 OrderAcceptedEvent
                OrderAcceptedEvent event = objectMapper.treeToValue(jsonNode, OrderAcceptedEvent.class);
                log.info("✅ [주문 수락] 사용자 ID: {}, 주문 ID: {}", event.getUserId(), event.getOrderId());
                // notificationService.sendToUser(event);
            }

        } catch (Exception e) {
            log.error("Redis 메시지 처리 중 에러 발생", e);
        }
    }

    private String createMessageForStatus(Order.Status status) {
        switch (status) {
            case REQUESTED:
                return "가게에서 주문을 수락했습니다.";
            case PREPARING:
                return "주문하신 음식이 조리되기 시작했습니다!";
            case READY_FOR_PICKUP:
                return "음식이 준비되었습니다. 곧 라이더가 픽업할 예정입니다.";
            case SHIPPING:
                return "배달이 시작되었습니다!";
            case COMPLETED:
                return "배달이 완료되었습니다. 맛있게 드세요!";
            case CANCELED:
                return "주문이 취소되었습니다.";
            default:
                return "주문 상태가 업데이트되었습니다.";
        }
    }
}