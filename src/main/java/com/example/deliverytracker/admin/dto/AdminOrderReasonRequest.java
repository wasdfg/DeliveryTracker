package com.example.deliverytracker.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AdminOrderReasonRequest {

    @NotBlank
    private String reason;
}
