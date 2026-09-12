package com.example.deliverytracker.notice.dto;

import lombok.Getter;

@Getter
public class NoticeSearchCondition {

    private String keyword;

    private Boolean important;

    private Boolean visible;
}
