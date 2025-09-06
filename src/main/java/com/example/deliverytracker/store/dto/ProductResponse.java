package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.Product;
import com.example.deliverytracker.store.entity.ProductCategory;
import lombok.Getter;

@Getter
public class ProductResponse {

    private Long productId;
    private String name;
    private int price;
    private ProductCategory category;
    private String description;
    private int stock;
    private String imageUrl;

    public ProductResponse(Long id, String name, int price,ProductCategory category,String description,int stock,String imageUrl) {
        this.productId = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCategory(), product.getDescription(),product.getStock(), product.getImageUrl());
    }
}
