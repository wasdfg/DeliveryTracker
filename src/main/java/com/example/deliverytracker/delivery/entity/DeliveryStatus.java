package com.example.deliverytracker.delivery.entity;

import java.util.EnumSet;

public enum DeliveryStatus {
    WAITING,      // 배차 대기
    ASSIGNED,     // 라이더 배정됨
    PICKED_UP,    // 라이더가 픽업 완료
    DELIVERING,   // 배달 중
    DELIVERED,    // 배달 완료
    FAILED        // 배달 실패    // 취소됨
    ;

    private static final EnumSet<DeliveryStatus> UPDATABLE_BY_RIDER =
            EnumSet.of(    PICKED_UP, DELIVERING   // 라이더가 픽업 완료
                    , DELIVERED, FAILED);

    public static boolean canUpdateByRider(DeliveryStatus status) {
        return UPDATABLE_BY_RIDER.contains(status);
    }
}