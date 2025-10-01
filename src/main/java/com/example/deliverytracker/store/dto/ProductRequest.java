package com.example.deliverytracker.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductRequest {

    @NotBlank
    private String name;

    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private BigDecimal price;

    @NotBlank
    private String description;

    @NotNull
    private String category;

    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    private int stock;
}
