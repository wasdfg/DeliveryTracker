package com.example.deliverytracker.cart.dto;

import java.math.BigDecimal;
import java.util.List;

public record RedisCartItem(String cartItemId, Long productId, String productName, BigDecimal basePrice, int quantity, List<RedisCartOption> options) {
    public RedisCartItem {
        if (quantity < 1) {
            throw new IllegalArgumentException("상품 수량은 1개 이상이어야 합니다.");
        }
    }

    public RedisCartItem withUpdatedQuantity(int newQuantity) {
        return new RedisCartItem(cartItemId, productId, productName, basePrice, newQuantity, options);
    }
}
