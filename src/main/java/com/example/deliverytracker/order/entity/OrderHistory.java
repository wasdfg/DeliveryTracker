package com.example.deliverytracker.order.entity;

import com.example.deliverytracker.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private Order.Status previousStatus;

    @Enumerated(EnumType.STRING)
    private Order.Status newStatus;

    private String changedBy;

    private String reason;

    public static OrderHistory create(Order order, Order.Status previousStatus, Order.Status newStatus, String changedBy, String reason){
        OrderHistory orderHistory = new OrderHistory();

        orderHistory.order = order;
        orderHistory.previousStatus = previousStatus;
        orderHistory.newStatus = newStatus;
        orderHistory.changedBy = changedBy;
        orderHistory.reason = reason;

        return  orderHistory;
    }
}