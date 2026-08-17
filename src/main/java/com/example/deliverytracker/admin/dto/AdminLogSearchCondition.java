package com.example.deliverytracker.admin.dto;

import com.example.deliverytracker.admin.entity.AdminAction;
import com.example.deliverytracker.admin.entity.TargetType;

import java.time.LocalDate;

public class AdminLogSearchCondition {
    private String adminKeyword;

    private TargetType targetType;

    private AdminAction action;

    private Long targetId;

    private LocalDate startDate;

    private LocalDate endDate;
}
