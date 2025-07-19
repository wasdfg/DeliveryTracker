package com.example.deliverytracker.delivery.entity;

import java.util.EnumSet;

public enum DeliveryStatus {
    REQUESTED,     // 요청됨
    ACCEPTED,      // 접수됨
    IN_PROGRESS,   // 배송 중
    DELIVERED,     // 배송 완료
    CANCELLED      // 취소됨
    ;

    private static final EnumSet<DeliveryStatus> UPDATABLE_BY_RIDER =
            EnumSet.of(IN_PROGRESS, DELIVERED, CANCELLED);

    public static boolean canUpdateByRider(DeliveryStatus status) {
        return UPDATABLE_BY_RIDER.contains(status);
    }
}