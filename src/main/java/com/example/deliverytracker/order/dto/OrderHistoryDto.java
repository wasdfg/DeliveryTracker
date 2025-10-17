package com.example.deliverytracker.order.dto;

import com.example.deliverytracker.order.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderHistoryDto {

    private Long orderId;
    private String storeName;
    private String storeImageUrl;
    private Order.Status status;
    private LocalDateTime requestedAt;
    private String totalPrice;

    public OrderHistoryDto(Order order) {
        this.orderId = order.getId();
        this.storeName = order.getStore().getName();
        this.storeImageUrl = order.getStore().getImageUrl();
        this.status = order.getStatus();
        this.requestedAt = order.getRequestedAt();
        this.totalPrice = String.format("%,d원", order.getTotalPrice().intValue());
    }
}
