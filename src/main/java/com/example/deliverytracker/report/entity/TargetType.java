package com.example.deliverytracker.report.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TargetType {

    REVIEW("리뷰"),
    ORDER("주문"),
    STORE("가게"),
    RIDER("라이더"),
    USER("회원");

    private final String description;
}
