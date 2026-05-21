package com.example.deliverytracker.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    NEW_ORDER("신규 주문", "새로운 주문이 접수되었습니다."),
    ORDER_STATUS_CHANGED("주문 상태 변경", "주문 상태가 변경되었습니다."),

    DELIVERY_STARTED("배달 시작", "라이더가 배달을 시작했습니다."),
    RIDER_ARRIVING("도착 예정", "라이더가 곧 도착합니다."),
    DELIVERY_COMPLETED("배달 완료", "배달이 완료되었습니다."),

    COMMENT_CREATED("댓글 등록", "새로운 댓글이 등록되었습니다."),
    REPLY_CREATED("대댓글 등록", "회원님의 댓글에 답글이 등록되었습니다."),
    REVIEW_ADDED("리뷰 등록", "매장에 새로운 리뷰가 등록되었습니다."),

    PASSWORD_RESET("비밀번호 재설정", "비밀번호 재설정 요청이 발생했습니다."),

    SYSTEM_NOTICE("공지사항", "시스템 공지사항이 등록되었습니다."),
    SYSTEM("시스템 알림", "시스템 알림이 도착했습니다.");

    private final String title;
    private final String defaultMessage;
}
