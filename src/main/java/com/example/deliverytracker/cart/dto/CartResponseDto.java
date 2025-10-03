package com.example.deliverytracker.cart.dto;

import com.example.deliverytracker.cart.entity.Cart;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CartResponseDto {
    private Long cartId;
    private List<CartItemResponseDto> cartItems;
    private BigDecimal totalPrice;

    public CartResponseDto() {
        this.cartId = null;
        this.cartItems = Collections.emptyList(); // 비어있는 리스트
        this.totalPrice = BigDecimal.ZERO;
    }

    public CartResponseDto(Cart cart) {
        this.cartId = cart.getId();
        this.cartItems = cart.getCartItems().stream()
                .map(CartItemResponseDto::new)
                .collect(Collectors.toList());
        this.totalPrice = calculateTotalPrice();
    }

    private BigDecimal calculateTotalPrice() {
        return cartItems.stream()
                .map(item -> item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
