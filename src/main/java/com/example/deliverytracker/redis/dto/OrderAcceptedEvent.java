package com.example.deliverytracker.redis.dto;

public class OrderAcceptedEvent {
    private final Long orderId;
    private final Long userId;
    private final String message;

    public OrderAcceptedEvent(Long orderId, Long userId) {
        this.orderId = orderId;
        this.userId = userId;
        this.message = "가게에서 주문을 수락했습니다. 조리가 시작됩니다!";
    }
}
