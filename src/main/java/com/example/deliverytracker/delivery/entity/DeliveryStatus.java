package com.example.deliverytracker.delivery.entity;

public enum DeliveryStatus {
    REQUESTED,     // 요청됨
    ACCEPTED,      // 접수됨
    IN_PROGRESS,   // 배송 중
    DELIVERED,     // 배송 완료
    CANCELLED      // 취소됨
    ;
}