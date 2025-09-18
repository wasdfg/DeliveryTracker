package com.example.deliverytracker.redis.dto;

public enum EventType {
    ORDER_CREATED,      // 주문 생성
    ORDER_ACCEPTED,     // 주문 수락
    ORDER_STATUS_CHANGED // 주문 상태 변경
}
