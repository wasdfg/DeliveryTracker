package com.example.deliverytracker.admin.dto;

import com.example.deliverytracker.store.entity.Product;

import java.math.BigDecimal;

public record AdminProductResponse(Long id, String name, BigDecimal price, String storeName, Boolean active, Boolean deleted) {

    public static AdminProductResponse from(Product product) {
        return new AdminProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStore().getName(),
                product.isAvailable(),
                product.isDelete()
        );
    }
}
