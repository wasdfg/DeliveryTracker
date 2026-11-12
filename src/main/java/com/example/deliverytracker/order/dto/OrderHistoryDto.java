package com.example.deliverytracker.order.dto;

import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.payment.dto.PaymentResponse;
import com.example.deliverytracker.payment.entity.Payment;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderHistoryDto {

    private Long orderId;

    private Long storeId;

    private String storeName;

    private String storeImageUrl;

    private Order.Status status;

    private LocalDateTime requestedAt;

    private BigDecimal totalPrice;

    private List<OrderItemResponse> orderItems;

    private PaymentResponse latestPayment;

    public OrderHistoryDto(Order order, Payment latestPayment) {
        this.orderId = order.getId();
        this.storeId = order.getStore().getId();
        this.storeName = order.getStore().getName();
        this.storeImageUrl = order.getStore().getImageUrl();
        this.status = order.getStatus();
        this.requestedAt = order.getRequestedAt();
        this.totalPrice = order.getTotalPrice();
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemResponse::from)
                .toList();
        this.latestPayment = latestPayment != null ? PaymentResponse.from(latestPayment) : null;
    }
}
