package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.StoreCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreSearchCondition {

    private String keyword;

    private StoreCategory category;

    private Integer minOrderAmount;

    private Integer deliveryFee;
}
