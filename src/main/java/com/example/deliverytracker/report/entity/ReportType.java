package com.example.deliverytracker.report.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportType {

    ABUSE("욕설/비방"),
    INAPPROPRIATE_CONTENT("부적절한 내용"),
    FALSE_INFORMATION("허위 정보"),
    ADVERTISEMENT("광고/홍보"),
    PRIVACY_VIOLATION("개인정보 노출"),
    ORDER_PROBLEM("주문 문제"),
    DELIVERY_PROBLEM("배송 문제"),
    STORE_PROBLEM("가게 문제"),
    RIDER_PROBLEM("라이더 문제"),
    OTHER("기타");

    private final String description;
}