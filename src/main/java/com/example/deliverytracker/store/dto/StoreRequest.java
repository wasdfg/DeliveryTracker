package com.example.deliverytracker.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.List;

@Getter
public class StoreRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String phone;

    private String description;

    @NotNull
    private BigDecimal minOrderAmount;

    @NotNull
    private int deliveryFee;

    private Long categoryId;

    private String imageUrl;

    private Boolean deleteImage;

    private List<OperationTimeRequest> operationTimes;

    @Getter
    public static class OperationTimeRequest {
        private DayOfWeek dayOfWeek;
        private String openTime;
        private String closeTime;
        private boolean isDayOff;
    }
}
