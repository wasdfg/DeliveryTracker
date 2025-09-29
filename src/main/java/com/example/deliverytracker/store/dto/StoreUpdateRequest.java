package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.StoreCategory;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class StoreUpdateRequest {

    private String name;

    private String address;

    private String phone;

    private String description;

    private String operatingHours;

    private BigDecimal minOrderAmount;

    private int deliveryFee;

    private Long categoryId;

    private String imageUrl;
}
