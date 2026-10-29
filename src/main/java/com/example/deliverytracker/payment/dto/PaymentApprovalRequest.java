package com.example.deliverytracker.payment.dto;


import java.math.BigDecimal;

public record PaymentApprovalRequest(String paymentKey, String orderId, BigDecimal amount) {
}