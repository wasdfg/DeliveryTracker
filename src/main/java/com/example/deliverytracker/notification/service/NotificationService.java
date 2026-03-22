package com.example.deliverytracker.notification.service;

import com.example.deliverytracker.config.SseConfig;
import com.example.deliverytracker.notification.dto.NotificationResponse;
import com.example.deliverytracker.notification.entity.Notification;
import com.example.deliverytracker.notification.entity.NotificationType;
import com.example.deliverytracker.notification.repository.NotificationRepository;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.entity.OrderStatusChangedEvent;
import com.example.deliverytracker.redis.RedisPublisher;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final SseConfig.SseEmitters sseEmitters;

    private final RedisPublisher redisPublisher;

    public SseEmitter subscribe(Long storeId) {
        SseEmitter emitter = new SseEmitter(60 * 1000L * 60);
        return sseEmitters.add(storeId, emitter);
    }

    public void sendNewOrderNotification(Long storeId,Long ownerId ,Long orderId) {
        String message = "새 주문이 접수되었습니다! (주문번호: " + orderId + ")";

        notificationRepository.save(Notification.builder()
                .receiverId(ownerId)
                .title("신규 주문")
                .content(message)
                .type(NotificationType.NEW_ORDER)
                .targetUrl("/store/" + storeId + "/orders")
                .build());

        sseEmitters.sendToStore(storeId, "newOrder", message);
    }

    public void notifyOrderStatusChanged(Long orderId, Long userId, Order.Status status) {

        String content = getStatusMessage(status);

        OrderStatusChangedEvent event = new OrderStatusChangedEvent(orderId, userId, status);

        notificationRepository.save(Notification.builder()
                .receiverId(userId)
                .title("주문 상태 업데이트")
                .content(content)
                .type(NotificationType.STATUS_CHANGED)
                .targetUrl("/orders/" + orderId)
                .build());

        redisPublisher.publish("order-channel", event);
    }

    private String getStatusMessage(Order.Status status) {
        return switch (status) {
            case ACCEPTED -> "가게에서 주문을 접수했습니다. 🍳";
            case DELIVERING -> "배달이 시작되었습니다. 🚀";
            case COMPLETED -> "배달이 완료되었습니다. 맛있게 드세요! 😋";
            case CANCELED -> "주문이 취소되었습니다. 😥";
            default -> "주문 상태가 변경되었습니다.";
        };
    }

    public List<NotificationResponse> getMyNotifications(Long userId) {
        return notificationRepository.findTop20ByReceiverIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @Transactional
    public void readNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("알림을 찾을 수 없습니다."));
        notification.markAsRead();
    }
    
    public Long getUnreadNotificationCount(Long userId) {
        return notificationRepository.countByReceiverIdAndIsReadFalse(userId);
    }
}
