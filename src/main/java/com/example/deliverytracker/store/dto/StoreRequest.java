package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.StoreCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class StoreRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String phone;

    private String description;

    private String operatingHours;

    @NotBlank
    private BigDecimal minOrderAmount;

    @NotBlank
    private int deliveryFee;

    @NotBlank
    private StoreCategory category;

    private String imageUrl;

    private Boolean deleteImage;
}
