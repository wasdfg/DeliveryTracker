package com.example.deliverytracker.order.entity;

import com.example.deliverytracker.common.BaseEntity;
import com.example.deliverytracker.delivery.entity.Delivery;
import com.example.deliverytracker.review.entity.Review;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.user.entitiy.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주문자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @BatchSize(size = 100)
    private Store store;

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private Review review;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private String pickupAddress;

    @Column(nullable = false)
    private String deliveryAddress;

    private Double deliveryLatitude;  // 배달지 위도
    private Double deliveryLongitude; // 배달지 경도

    @Column(nullable = false)
    private BigDecimal totalPrice;

    private LocalDateTime requestedAt;
    private LocalDateTime canceledAt;

    private boolean deleted;

    @Column(name = "estimated_delivery_time")
    private String estimatedDeliveryTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    public enum Status {
        REQUESTED,     // 주문 요청됨
        PREPARING,          // 음식 조리 중
        READY_FOR_PICKUP,   // 라이더 픽업 대기
        SHIPPING,           // 배달 중
        COMPLETED,          // 배달완료
        CANCELED            // 주문 취소
    }

    public void cancel(Status status) {
        this.status = status;
        if (status == Status.CANCELED) {
            this.canceledAt = LocalDateTime.now();
        }
    }

    public void registerDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void changeStatus(Status newStatus) {
        this.status = newStatus;
    }

    public void delete(boolean deleted){
        this.deleted = deleted;
    }
}