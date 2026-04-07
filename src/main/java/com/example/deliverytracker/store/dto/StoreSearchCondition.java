package com.example.deliverytracker.store.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreSearchCondition {

    private String keyword;

    private Long categoryId;

    private Integer minOrderAmount;

    private Integer deliveryFee;

    //카데고리 가게 유사명도 나오도록 조회
}
