package com.example.deliverytracker.admin.dto;

import com.example.deliverytracker.order.entity.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AdminOrderStatusRequest {

    @NotNull
    private Order.Status status;

    @NotBlank
    private String reason;
}
