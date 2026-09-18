package com.example.deliverytracker.report.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReportProcessRequest {

    @NotBlank(message = "처리 내용을 입력해주세요.")
    private String comment;

    private boolean suspendUser;

    @Min(value = 1, message = "정지 기간은 1일 이상이어야 합니다.")
    private Integer suspensionDays;
}
