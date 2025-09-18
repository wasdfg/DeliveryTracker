package com.example.deliverytracker.redis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class OrderCreatedEvent implements Serializable {
    private Long orderId;
    private Long storeId;
    private String message;

    public OrderCreatedEvent(Long orderId, Long storeId) {
        this.orderId = orderId;
        this.storeId = storeId;
        this.message = "새로운 주문이 들어왔습니다!";
    }
}