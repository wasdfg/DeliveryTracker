package com.example.deliverytracker.order.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class OrderCreatedEvent implements Serializable {
    private Long orderId;
    private Long userId;

    public OrderCreatedEvent(Long orderId, Long userId) {
        this.orderId = orderId;
        this.userId = userId;
    }
}