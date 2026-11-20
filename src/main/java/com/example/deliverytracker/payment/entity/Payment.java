package com.example.deliverytracker.payment.entity;

import com.example.deliverytracker.order.entity.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "payments",
        indexes = {
                @Index(name = "idx_payment_order_id", columnList = "order_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(unique = true, length = 200)
    private String paymentKey;

    @Column(nullable = false, length = 30)
    private String method;

    @Column(name = "gateway_order_id", nullable = false, unique = true, length = 64, updatable = false)
    private String gatewayOrderId;

    @Column(length = 500)
    private String failureReason;

    @Column(name = "last_error", length = 500)
    private String lastError;

    private LocalDateTime approvedAt;

    private LocalDateTime canceledAt;

    private LocalDateTime refundedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static Payment create(Order order, BigDecimal amount, String method) {
        if (order == null) {
            throw new IllegalArgumentException("결제 대상 주문은 필수입니다.");
        }

        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("결제 금액은 0 이상이어야 합니다.");
        }

        if (method == null || method.isBlank()) {
            throw new IllegalArgumentException("결제 수단은 필수입니다.");
        }

        Payment payment = new Payment();

        payment.order = order;
        payment.amount = amount;
        payment.method = method;
        payment.status = PaymentStatus.READY;
        payment.gatewayOrderId = "DT-" + UUID.randomUUID();

        return payment;
    }

    public void approve(String paymentKey) {

        validateStatus(PaymentStatus.APPROVING, PaymentStatus.APPROVAL_UNKNOWN);

        validatePaymentKey(paymentKey);

        if (!this.paymentKey.equals(paymentKey)) {
            throw new IllegalArgumentException("PG 결제 키가 일치하지 않습니다.");
        }

        this.status = PaymentStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();

        this.failureReason = null;
        this.lastError = null;
    }

    public void fail(String failureReason) {

        validateStatus(PaymentStatus.APPROVING, PaymentStatus.APPROVAL_UNKNOWN);

        this.status = PaymentStatus.FAILED;
        this.failureReason = normalizeReason(failureReason);
        this.lastError = null;
    }

    public void cancel() {
        validateStatus(PaymentStatus.READY);

        this.status = PaymentStatus.CANCELED;
        this.canceledAt = LocalDateTime.now();
    }

    public void refund() {

        validateStatus(PaymentStatus.REFUNDING, PaymentStatus.REFUND_UNKNOWN);

        this.status = PaymentStatus.REFUNDED;
        this.refundedAt = LocalDateTime.now();

        this.lastError = null;
    }

    public void startApproval(String paymentKey) {

        validateStatus(PaymentStatus.READY);
        validatePaymentKey(paymentKey);

        this.paymentKey = paymentKey;
        this.status = PaymentStatus.APPROVING;

        this.failureReason = null;
        this.lastError = null;
    }

    public void markApprovalUnknown(String reason) {

        validateStatus(PaymentStatus.APPROVING);

        this.status = PaymentStatus.APPROVAL_UNKNOWN;
        this.lastError = normalizeReason(reason);
    }

    public void startRefund() {

        validateStatus(PaymentStatus.APPROVED);

        this.status = PaymentStatus.REFUNDING;
        this.lastError = null;
    }

    public void markRefundUnknown(String reason) {

        validateStatus(PaymentStatus.REFUNDING);

        this.status = PaymentStatus.REFUND_UNKNOWN;
        this.lastError = normalizeReason(reason);
    }

    public void failRefund(String reason) {

        validateStatus(PaymentStatus.REFUNDING, PaymentStatus.REFUND_UNKNOWN);

        this.status = PaymentStatus.APPROVED;
        this.lastError = normalizeReason(reason);
    }



    private void validateStatus(PaymentStatus... expectedStatuses) {

        for (PaymentStatus expectedStatus : expectedStatuses) {

            if (this.status == expectedStatus) {
                return;
            }
        }

        throw new IllegalStateException("현재 결제 상태에서는 해당 작업을 수행할 수 없습니다. " + "현재 상태: " + this.status);
    }

    private void validatePaymentKey(String paymentKey) {

        if (paymentKey == null || paymentKey.isBlank()) {
            throw new IllegalArgumentException("결제 키는 필수입니다.");
        }
    }

    private String normalizeReason(String reason) {

        if (reason == null || reason.isBlank()) {
            return "상세 오류 사유를 확인할 수 없습니다.";
        }

        return reason.length() > 500 ? reason.substring(0, 500) : reason;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}