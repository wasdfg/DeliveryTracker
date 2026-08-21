package com.example.deliverytracker.admin.dto;

import com.example.deliverytracker.order.dto.OrderItemResponse;
import com.example.deliverytracker.order.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class AdminOrderDetailResponse {

    private Long orderId;

    private String userNickname;

    private String userPhone;

    private String storeName;

    private Order.Status status;

    private Long totalPrice;

    private LocalDateTime createdAt;

    private String deliveryAddress;

    private List<OrderItemResponse> items;

    public AdminOrderDetailResponse(Long orderId, String userNickname, String userPhone, String storeName, Order.Status status,
            Long totalPrice, LocalDateTime createdAt, String deliveryAddress, List<OrderItemResponse> items) {
        this.orderId = orderId;
        this.userNickname = userNickname;
        this.userPhone = userPhone;
        this.storeName = storeName;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.deliveryAddress = deliveryAddress;
        this.items = items;
    }
}
