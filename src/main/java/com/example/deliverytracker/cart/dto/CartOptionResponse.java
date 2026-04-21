package com.example.deliverytracker.cart.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CartOptionResponse {
    private Long optionId;
    private String name;
    private BigDecimal additionalPrice;

    public CartOptionResponse(RedisCartOption redisOption) {
        this.optionId = redisOption.optionId();
        this.name = redisOption.name();
        this.additionalPrice = redisOption.additionalPrice();
    }
}
