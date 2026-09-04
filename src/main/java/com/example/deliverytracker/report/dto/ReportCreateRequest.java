package com.example.deliverytracker.report.dto;

import com.example.deliverytracker.report.entity.ReportType;
import com.example.deliverytracker.report.entity.TargetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReportCreateRequest {

    @NotNull(message = "신고 대상을 선택해주세요.")
    private TargetType targetType;

    @NotNull(message = "신고 대상이 없습니다.")
    private Long targetId;

    @NotNull(message = "신고 유형을 선택해주세요.")
    private ReportType type;

    @NotBlank(message = "신고 내용을 입력해주세요.")
    private String description;
}