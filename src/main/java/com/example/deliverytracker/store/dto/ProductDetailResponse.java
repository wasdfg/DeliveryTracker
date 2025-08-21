package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.ProductCategory;
import lombok.Getter;

@Getter
public class ProductDetailResponse {

    private String name;

    private int price;

    private String description;

    private ProductCategory category;

    private int stock;


    public ProductDetailResponse(String name, int price, String description,ProductCategory category, int stock) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.stock = stock;
    }

}
