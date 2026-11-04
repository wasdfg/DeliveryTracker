package com.example.deliverytracker.payment.dto;

import com.example.deliverytracker.payment.entity.Payment;
import com.example.deliverytracker.payment.entity.PaymentStatus;

import java.math.BigDecimal;

public record PaymentResponse(Long paymentId, Long orderId, String gatewayOrderId, BigDecimal amount, String method, PaymentStatus status) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(payment.getId(),
                payment.getOrder().getId(),
                payment.getGatewayOrderId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus()
        );
    }
}