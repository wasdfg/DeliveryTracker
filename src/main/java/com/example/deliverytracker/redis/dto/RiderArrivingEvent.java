package com.example.deliverytracker.redis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RiderArrivingEvent {
    private Long orderId;
    private Long userId;
    private String message = "배달 기사님이 곧 도착합니다! 받을 준비를 해주세요.";

    public RiderArrivingEvent(Long orderId, Long userId) {
        this.orderId = orderId;
        this.userId = userId;
    }
}
