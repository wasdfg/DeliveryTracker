package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.Category;
import com.example.deliverytracker.store.entity.StoreCategory;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private BigDecimal minOrderAmount;

    @NotBlank
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
