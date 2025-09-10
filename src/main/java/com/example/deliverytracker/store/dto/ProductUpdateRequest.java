package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.ProductCategory;
import lombok.Getter;

@Getter
public class ProductUpdateRequest {
    private String name;
    private Integer price;
    private String description;
    private ProductCategory category;
    private Integer stock;
    private String imageUrl;
    private Boolean deleteImage;
}
