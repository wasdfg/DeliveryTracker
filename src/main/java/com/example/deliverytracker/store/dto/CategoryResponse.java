package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.Category;
import lombok.Getter;

@Getter
public class CategoryResponse {

    private String name;

    public CategoryResponse(String name) {
        this.name = name;
    }

    public static CategoryResponse from(Category category) {

        return new CategoryResponse(
                category.getName()
        );
    }
}
