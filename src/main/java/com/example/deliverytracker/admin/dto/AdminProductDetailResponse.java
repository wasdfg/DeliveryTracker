package com.example.deliverytracker.admin.dto;

import com.example.deliverytracker.store.entity.Product;

import java.math.BigDecimal;

public record AdminProductDetailResponse(Long id, String name, String description, BigDecimal price, String storeName, Boolean active, Boolean deleted) {

    public static AdminProductDetailResponse from(Product product) {
        return new AdminProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStore().getName(),
                product.isAvailable(),
                product.isDelete()
        );
    }
}