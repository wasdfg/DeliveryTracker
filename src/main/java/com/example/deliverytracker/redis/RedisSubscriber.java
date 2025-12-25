package com.example.deliverytracker.redis;

import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.entity.OrderStatusChangedEvent;
import com.example.deliverytracker.redis.dto.DeliveryStartedEvent;
import com.example.deliverytracker.redis.dto.NewReviewEvent;
import com.example.deliverytracker.redis.dto.OrderAcceptedEvent;
import com.example.deliverytracker.redis.dto.OrderCreatedEvent;
import com.example.deliverytracker.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishedMessage = new String(message.getBody());
            JsonNode jsonNode = objectMapper.readTree(publishedMessage);

            if (jsonNode.has("storeId") && !jsonNode.has("newStatus")) {
                OrderCreatedEvent event = objectMapper.treeToValue(jsonNode, OrderCreatedEvent.class);
                log.info("✅ [주문 생성] 가게 ID: {}, 주문 ID: {}", event.getStoreId(), event.getOrderId());

                messagingTemplate.convertAndSend("/topic/store/" + event.getStoreId(), event);
            }

            else if (jsonNode.has("newStatus")) {
                OrderStatusChangedEvent event = objectMapper.treeToValue(jsonNode, OrderStatusChangedEvent.class);
                String notiMessage = createMessageForStatus(event.getNewStatus());
                log.info("✅ [상태 변경] 사용자 ID: {}, 메시지: {}", event.getUserId(), notiMessage);

                messagingTemplate.convertAndSend("/topic/user/" + event.getUserId(), event);

                userRepository.findById(event.getUserId()).ifPresent(user -> {
                    notificationService.sendNotification(user.getFcmToken(), "주문 상태 변경", notiMessage);
                });
            }

            else if (jsonNode.has("riderName")) {
                DeliveryStartedEvent event = objectMapper.treeToValue(jsonNode, DeliveryStartedEvent.class);
                log.info("✅ [배달 시작] 사용자 ID: {}, 라이더: {}", event.getUserId(), event.getRiderName());

                messagingTemplate.convertAndSend("/topic/user/" + event.getUserId(), event);
            }

            else if (jsonNode.has("userId") && !jsonNode.has("storeId")) {
                OrderAcceptedEvent event = objectMapper.treeToValue(jsonNode, OrderAcceptedEvent.class);

                messagingTemplate.convertAndSend("/topic/user/" + event.getUserId(), event);
            }

            else if (jsonNode.has("authorName")) {
                NewReviewEvent event = objectMapper.treeToValue(jsonNode, NewReviewEvent.class);
                log.info("✅ [새 리뷰] 가게 ID: {}, 작성자: {}", event.getStoreId(), event.getAuthorName());

                messagingTemplate.convertAndSend("/topic/store/" + event.getStoreId(), event);
            }

        } catch (Exception e) {
            log.error("Redis 메시지 처리 중 에러 발생", e);
        }
    }

    private String createMessageForStatus(Order.Status status) {

        switch (status) {
            case REQUESTED: return "가게에서 주문을 확인 중입니다.";
            case PREPARING: return "주문하신 음식이 조리되기 시작했습니다!";
            case SHIPPING: return "배달이 시작되었습니다! 🏍️";
            case COMPLETED: return "배달이 완료되었습니다. 맛있게 드세요! 😋";
            case CANCELED: return "주문이 취소되었습니다.";
            default: return "주문 상태가 업데이트되었습니다.";
        }
    }
}