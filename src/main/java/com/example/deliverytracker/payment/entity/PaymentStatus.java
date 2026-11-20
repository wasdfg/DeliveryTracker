package com.example.deliverytracker.payment.entity;

public enum PaymentStatus {

    READY,      // 결제 대기
    APPROVED,   // 결제 승인 완료
    FAILED,     // 결제 실패
    CANCELED,   // 결제 승인 전 취소
    REFUNDED,    // 승인된 결제의 환불 완료
    APPROVING,  //PG 승인 요청 진행 중
    APPROVAL_UNKNOWN, //PG 승인 결과 불확실
    REFUNDING, //PG 환불 요청 진행 중
    REFUND_UNKNOWN, //PG 환불 결과 불확실
}