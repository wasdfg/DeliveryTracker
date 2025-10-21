package com.example.deliverytracker.order.dto;

import com.example.deliverytracker.order.entity.OrderItem;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderItemResponse {
    private String menuName;
    private int quantity;
    private BigDecimal price;

    public OrderItemResponse(String name, int quantity, BigDecimal price) {
        this.menuName = name;
        this.quantity = quantity;
        this.price = price;
    }

    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPrice()
        );
    }
}
