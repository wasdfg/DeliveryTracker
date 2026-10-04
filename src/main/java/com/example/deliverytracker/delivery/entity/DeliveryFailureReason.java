package com.example.deliverytracker.delivery.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryFailureReason {

    CUSTOMER_ABSENT("고객 부재"),
    CONTACT_UNAVAILABLE("연락 불가"),
    INVALID_ADDRESS("주소 오류"),
    CUSTOMER_REFUSED("수령 거부"),
    OTHER("기타");

    private final String description;
}