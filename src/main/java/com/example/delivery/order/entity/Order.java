package com.example.delivery.order.entity;

import com.example.delivery.rider.entity.Rider;
import com.example.delivery.user.entitiy.User;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주문자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 배달자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id")
    private Rider rider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private String pickupAddress;

    @Column(nullable = false)
    private String deliveryAddress;

    @Column(nullable = false)
    private int totalPrice;

    private LocalDateTime requestedAt;
    private LocalDateTime deliveredAt;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    public enum Status {
        REQUESTED,     // 주문 요청됨
        ASSIGNED,      // 라이더 배정됨
        DELIVERING,    // 배달 중
        DELIVERED,     // 배달 완료
        CANCELED       // 주문 취소됨
    }

    @PrePersist
    public void prePersist() {
        this.status = Status.REQUESTED;
        this.requestedAt = LocalDateTime.now();
    }

    public void assignRider(Rider rider) {
        this.rider = rider;
        this.status = Status.ASSIGNED;
    }

    public void updateStatus(Status status) {
        this.status = status;
        if (status == Status.DELIVERED) {
            this.deliveredAt = LocalDateTime.now();
        }
    }
}