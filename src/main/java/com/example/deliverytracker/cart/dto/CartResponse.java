package com.example.deliverytracker.cart.dto;

import com.example.deliverytracker.cart.entity.Cart;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CartResponse {

    private List<CartItemResponse> cartItems;

    private BigDecimal totalPrice;


    public CartResponse() {
        this.cartItems = Collections.emptyList();
        this.totalPrice = BigDecimal.ZERO;
    }

    public CartResponse(List<RedisCartItem> items) {
        if (items == null || items.isEmpty()) {
            this.cartItems = Collections.emptyList();
            this.totalPrice = BigDecimal.ZERO;
            return;
        }

        this.cartItems = items.stream()
                .map(CartItemResponse::new)
                .collect(Collectors.toList());

        this.totalPrice = calculateTotalPrice();
    }

    private BigDecimal calculateTotalPrice() {
        return cartItems.stream()
                // 단위 가격(옵션 포함) * 수량
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
