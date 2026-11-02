package com.example.deliverytracker.payment.service;


import com.example.deliverytracker.payment.dto.PaymentApprovalResult;
import com.example.deliverytracker.payment.dto.PaymentRefundResult;
import com.example.deliverytracker.payment.entity.Payment;
import com.example.deliverytracker.payment.entity.PaymentStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(
        name = "deliverytracker.payment.provider",
        havingValue = "mock",
        matchIfMissing = true
)
public class MockPaymentGateway implements PaymentGateway{

    private final boolean simulateFailure;

    @Value("${deliverytracker.payment.mock.refund-fail:false}")
    boolean simulateRefundFailure;

    public MockPaymentGateway(@Value("${deliverytracker.payment.mock.fail:false}") boolean simulateFailure) {
        this.simulateFailure = simulateFailure;
    }

    @Override
    public PaymentApprovalResult approve(Payment payment, String providerPaymentKey) {
        if (payment.getStatus() != PaymentStatus.READY) {
            throw new IllegalStateException("결제 대기 상태에서만 승인할 수 있습니다.");
        }

        if (simulateFailure) {
            return PaymentApprovalResult.failed("Mock 결제 승인에 실패했습니다.");
        }

        String paymentKey = "MOCK-" + UUID.randomUUID();

        return PaymentApprovalResult.approved(paymentKey, payment.getGatewayOrderId(), payment.getAmount());
    }

    @Override
    public PaymentRefundResult refund(Payment payment, String refundReason) {
        if (payment.getStatus() != PaymentStatus.APPROVED) {
            throw new IllegalStateException("승인된 결제만 환불할 수 있습니다.");
        }

        if (simulateRefundFailure) {
            return PaymentRefundResult.failed("Mock 환불 처리에 실패했습니다.");
        }

        return PaymentRefundResult.refunded(payment.getPaymentKey(), payment.getAmount());
    }
}
