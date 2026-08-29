package com.example.deliverytracker.admin.dto;

import com.example.deliverytracker.order.entity.Order;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AdminOrderSearchCondition {
    private Long orderId;

    private String userKeyword;

    private String storeKeyword;

    private Order.Status status;

    private LocalDate startDate;

    private LocalDate endDate;
}
