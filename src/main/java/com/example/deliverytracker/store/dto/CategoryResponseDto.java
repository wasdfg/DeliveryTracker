package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.Category;
import lombok.Getter;

@Getter
public class CategoryResponseDto {
    private Long id;
    private String name;

    public CategoryResponseDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

    public static CategoryResponseDto from(Category category) {
        if (category == null) return null;
        return new CategoryResponseDto(category);
    }
}