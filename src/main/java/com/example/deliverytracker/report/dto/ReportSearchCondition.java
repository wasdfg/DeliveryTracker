package com.example.deliverytracker.report.dto;


import com.example.deliverytracker.report.entity.ReportStatus;
import com.example.deliverytracker.report.entity.ReportType;
import com.example.deliverytracker.report.entity.TargetType;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReportSearchCondition {

    private TargetType targetType;

    private ReportType reportType;

    private ReportStatus status;

    private Long targetId;

    private String reporterKeyword;

    private LocalDate startDate;

    private LocalDate endDate;
}
