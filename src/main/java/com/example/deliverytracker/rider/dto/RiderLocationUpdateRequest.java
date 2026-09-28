package com.example.deliverytracker.rider.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RiderLocationUpdateRequest {

    @NotNull(message = "주문 ID가 없습니다.")
    private Long orderId;

    @NotNull(message = "위도가 없습니다.")
    @DecimalMin(value = "-90.0", message = "위도 값이 올바르지 않습니다.")
    @DecimalMax(value = "90.0", message = "위도 값이 올바르지 않습니다.")
    private Double latitude;

    @NotNull(message = "경도가 없습니다.")
    @DecimalMin(value = "-180.0", message = "경도 값이 올바르지 않습니다.")
    @DecimalMax(value = "180.0", message = "경도 값이 올바르지 않습니다.")
    private Double longitude;
}
