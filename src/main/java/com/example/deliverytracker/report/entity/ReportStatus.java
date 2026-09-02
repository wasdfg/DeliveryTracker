package com.example.deliverytracker.report.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportStatus {

    WAITING("접수"),
    IN_PROGRESS("처리중"),
    RESOLVED("처리완료"),
    REJECTED("반려");

    private final String description;
}