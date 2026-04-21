package com.example.deliverytracker.cart.dto;

import java.math.BigDecimal;

public record RedisCartOption(Long optionId, String name, BigDecimal additionalPrice) {}