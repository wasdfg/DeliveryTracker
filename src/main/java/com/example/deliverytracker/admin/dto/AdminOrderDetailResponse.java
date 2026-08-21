package com.example.deliverytracker.admin.dto;

import com.example.deliverytracker.order.dto.OrderItemResponse;
import com.example.deliverytracker.order.entity.Order;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class AdminOrderDetailResponse {

    private Long orderId;

    private String userNickname;

    private String userPhone;

    private String storeName;

    private Order.Status status;

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;

    private String deliveryAddress;

    private List<OrderItemResponse> items;

    public AdminOrderDetailResponse(Long orderId, String userNickname, String userPhone, String storeName, Order.Status status,
            BigDecimal totalPrice, LocalDateTime createdAt, String deliveryAddress, List<OrderItemResponse> items) {
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

    public static AdminOrderDetailResponse from(Order order) {
        return new AdminOrderDetailResponse(
                order.getId(),
                order.getUser().getNickname(),
                order.getUser().getPhone(),
                order.getStore().getName(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getDeliveryAddress(),
                OrderItemResponse.from(order.getOrderItems())
        );
    }

}
