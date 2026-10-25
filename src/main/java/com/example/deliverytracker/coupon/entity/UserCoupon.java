package com.example.deliverytracker.coupon.entity;

import com.example.deliverytracker.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(indexes = {
        @Index(name = "idx_user_coupon_reserved_order",
                columnList = "reserved_order_id")
})
@NoArgsConstructor
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(nullable = false)
    private boolean isUsed = false;

    private LocalDateTime usedAt;

    @Column(name = "reserved_order_id")
    private Long reservedOrderId;

    private LocalDateTime reservedAt; //결제대기용

    public UserCoupon(User user, Coupon coupon) {
        this.user = user;
        this.coupon = coupon;
        this.isUsed = false;
    }

    public void reserve(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("쿠폰을 예약할 주문 ID가 필요합니다.");
        }

        if (isUsed) {
            throw new IllegalStateException("이미 사용된 쿠폰입니다.");
        }


        if (reservedOrderId != null && !reservedOrderId.equals(orderId)) {
            throw new IllegalStateException("다른 주문에서 예약한 쿠폰입니다.");
        }

        if (reservedOrderId != null) {
            return;
        }

        this.reservedOrderId = orderId;
        this.reservedAt = LocalDateTime.now();
    }

    public void use(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("쿠폰을 사용한 주문 ID가 필요합니다.");
        }

        if (isUsed) {
            throw new IllegalStateException("이미 사용된 쿠폰입니다.");
        }

        if (reservedOrderId == null || !reservedOrderId.equals(orderId)) {
            throw new IllegalStateException("해당 주문에서 예약한 쿠폰이 아닙니다.");
        }

        this.isUsed = true;
        this.usedAt = LocalDateTime.now();

        this.reservedOrderId = null;
        this.reservedAt = null;
    }

    public void releaseReservation(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("예약을 해제할 주문 ID가 필요합니다.");
        }

        if (isUsed) {
            throw new IllegalStateException("이미 사용 완료된 쿠폰은 예약 해제할 수 없습니다.");
        }

        if (reservedOrderId == null) {
            return;
        }

        if (!reservedOrderId.equals(orderId)) {
            throw new IllegalStateException("해당 주문에서 예약한 쿠폰이 아닙니다.");
        }

        this.reservedOrderId = null;
        this.reservedAt = null;
    }
}
