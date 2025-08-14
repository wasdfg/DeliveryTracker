package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.Product;
import lombok.Getter;

@Getter
public class ProductResponse {

    private Long productId;
    private String name;
    private int price;

    public ProductResponse(Long id, String name, int price) {
        this.productId = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}
