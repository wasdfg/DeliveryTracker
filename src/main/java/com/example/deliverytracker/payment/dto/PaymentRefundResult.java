package com.example.deliverytracker.payment.dto;

import java.math.BigDecimal;

public record PaymentRefundResult(boolean success, String paymentKey, BigDecimal refundedAmount, String failureReason) {
    public static PaymentRefundResult refunded(String paymentKey, BigDecimal refundedAmount) {
        return new PaymentRefundResult(true, paymentKey, refundedAmount, null);
    }

    public static PaymentRefundResult failed(String reason) {
        return new PaymentRefundResult(false, null, null, reason);
    }
}
