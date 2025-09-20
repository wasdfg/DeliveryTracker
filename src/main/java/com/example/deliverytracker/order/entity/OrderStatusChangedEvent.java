package com.example.deliverytracker.order.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class OrderStatusChangedEvent implements Serializable {

    private Long orderId;
    private Long userId;
    private Order.Status newStatus;

    public OrderStatusChangedEvent(Long orderId, Long userId, Order.Status newStatus) {
        this.orderId = orderId;
        this.userId = userId;
        this.newStatus = newStatus;
    }
}