package com.example.deliverytracker.redis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class DeliveryStartedEvent implements Serializable {
    private Long orderId;
    private Long userId;
    private String riderName;
    private String message;

    public DeliveryStartedEvent(Long orderId, Long userId, String riderName) {
        this.orderId = orderId;
        this.userId = userId;
        this.riderName = riderName;
        this.message = riderName + " 라이더가 배달을 시작했습니다!";
    }
}
