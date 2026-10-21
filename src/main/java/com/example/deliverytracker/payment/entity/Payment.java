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

    @Column(length = 500)
    private String failureReason;

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

        return payment;
    }

    public void approve(String paymentKey) {
        validateStatus(PaymentStatus.READY);

        if (paymentKey == null || paymentKey.isBlank()) {
            throw new IllegalArgumentException("결제 키는 필수입니다.");
        }

        this.paymentKey = paymentKey;
        this.status = PaymentStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
        this.failureReason = null;
    }

    public void fail(String failureReason) {
        validateStatus(PaymentStatus.READY);

        this.status = PaymentStatus.FAILED;
        this.failureReason = failureReason;
    }

    public void cancel() {
        validateStatus(PaymentStatus.READY);

        this.status = PaymentStatus.CANCELED;
        this.canceledAt = LocalDateTime.now();
    }

    public void refund() {
        validateStatus(PaymentStatus.APPROVED);

        this.status = PaymentStatus.REFUNDED;
        this.refundedAt = LocalDateTime.now();
    }

    private void validateStatus(PaymentStatus expectedStatus) {
        if (this.status != expectedStatus) {
            throw new IllegalStateException("현재 결제 상태에서는 해당 작업을 수행할 수 없습니다. " + "현재 상태: " + this.status);
        }
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