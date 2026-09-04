package com.example.deliverytracker.report.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReportProcessRequest {

    @NotBlank(message = "처리 내용을 입력해주세요.")
    private String comment;
}
