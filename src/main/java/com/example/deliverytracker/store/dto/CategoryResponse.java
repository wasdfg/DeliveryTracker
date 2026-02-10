package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.Category;
import lombok.Getter;

@Getter
public class CategoryResponse {
    private Long id;
    private String name;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

    public static CategoryResponse from(Category category) {
        if (category == null) return null;
        return new CategoryResponse(category);
    }
}