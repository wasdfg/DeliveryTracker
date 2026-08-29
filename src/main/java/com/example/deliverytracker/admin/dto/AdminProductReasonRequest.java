package com.example.deliverytracker.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminProductReasonRequest {

    @NotNull
    private Boolean active;

    @NotBlank
    private String reason;
}
