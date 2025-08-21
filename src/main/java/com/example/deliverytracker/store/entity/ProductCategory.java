package com.example.deliverytracker.store.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductCategory {

    MAIN_DISH("메인 메뉴"),
    SIDE_DISH("사이드 메뉴"),
    BEVERAGE("음료");

    private final String description;
}