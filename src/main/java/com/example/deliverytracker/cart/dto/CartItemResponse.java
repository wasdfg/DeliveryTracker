package com.example.deliverytracker.cart.dto;

import com.example.deliverytracker.cart.entity.CartItem;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CartItemResponse {
    private String cartItemId;
    private Long productId;
    private String productName;
    private String imageUrl;
    private Long storeId;
    private BigDecimal deliveryFee;
    private BigDecimal minOrderAmount;
    private BigDecimal unitPrice;
    private int quantity;
    private List<CartOptionResponse> options;
    public CartItemResponse(RedisCartItem redisItem) {
        this.cartItemId = redisItem.cartItemId();
        this.productId = redisItem.productId();
        this.productName = redisItem.productName();
        this.imageUrl = redisItem.imageUrl();
        this.storeId = redisItem.storeId();
        this.deliveryFee = redisItem.deliveryFee();
        this.minOrderAmount = redisItem.minOrderAmount();
        this.quantity = redisItem.quantity();

        this.options = redisItem.options() != null ?
                redisItem.options().stream()
                        .map(CartOptionResponse::new)
                        .collect(Collectors.toList()) : List.of();

        BigDecimal optionTotal = this.options.stream()
                .map(CartOptionResponse::getAdditionalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.unitPrice = redisItem.basePrice().add(optionTotal);
    }
}
