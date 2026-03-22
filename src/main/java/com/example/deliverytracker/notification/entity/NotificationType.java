package com.example.deliverytracker.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    NEW_ORDER("신규 주문", "새로운 주문이 접수되었습니다."),
    STATUS_CHANGED("주문 상태 변경", "주문의 배달 상태가 업데이트되었습니다."),
    REVIEW_ADDED("리뷰 등록", "매장에 새로운 리뷰가 등록되었습니다."),
    SYSTEM_NOTICE("공지사항", "시스템 점검 안내입니다.");

    private final String title;
    private final String defaultMessage;
}
