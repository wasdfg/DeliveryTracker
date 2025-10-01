package com.example.deliverytracker.cart.dto;

import com.example.deliverytracker.cart.entity.CartItem;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CartItemResponseDto {
    private Long cartItemId;
    private String productName;
    private BigDecimal productPrice;
    private int quantity;
    private String imageUrl;

    public CartItemResponseDto(CartItem cartItem) {
        this.cartItemId = cartItem.getId();
        this.productName = cartItem.getProduct().getName();
        this.productPrice = cartItem.getProduct().getPrice();
        this.quantity = cartItem.getQuantity();
        this.imageUrl = cartItem.getProduct().getImageUrl();
    }
}
