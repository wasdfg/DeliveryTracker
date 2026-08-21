package com.example.deliverytracker.admin.dto;

import com.example.deliverytracker.order.entity.Order;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class AdminOrderResponse {

    private Long orderId;

    private String userNickname;

    private String storeName;

    private Order.Status status;

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;

    public AdminOrderResponse(Long orderId, String userNickname, String storeName, Order.Status status, BigDecimal totalPrice, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.userNickname = userNickname;
        this.storeName = storeName;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public static AdminOrderResponse from(Order order) {
        return new AdminOrderResponse(
                order.getId(),
                order.getUser().getNickname(),
                order.getStore().getName(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getCreatedAt()
        );
    }
}
