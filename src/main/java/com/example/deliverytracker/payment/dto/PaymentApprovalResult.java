package com.example.deliverytracker.payment.dto;

import java.math.BigDecimal;

public record PaymentApprovalResult(boolean success, String paymentKey, String orderId, BigDecimal approvedAmount, String failureReason) {

    public static PaymentApprovalResult approved(String paymentKey, String orderId, BigDecimal approvedAmount) {
        return new PaymentApprovalResult(true, paymentKey, orderId, approvedAmount, null);
    }

    public static PaymentApprovalResult failed(String reason) {
        return new PaymentApprovalResult(false, null, null, null, reason);
    }
}
