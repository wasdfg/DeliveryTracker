package com.example.deliverytracker.store.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreCategory {

    KOREAN_FOOD("한식"),
    CHINESE_FOOD("중식"),
    JAPANESE_FOOD("일식"),
    WESTERN_FOOD("양식"),
    CHICKEN("치킨"),
    PIZZA("피자"),
    SNACK_FOOD("분식"),
    CAFE_DESSERT("카페/디저트"),
    ASIAN("아시안"),
    FAST_FOOD("패스트푸드");

    private final String description;
}