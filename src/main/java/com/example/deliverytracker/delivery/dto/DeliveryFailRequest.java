package com.example.deliverytracker.delivery.dto;

import com.example.deliverytracker.delivery.entity.DeliveryFailureReason;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DeliveryFailRequest {

    @NotNull(message = "배송 실패 사유를 선택해주세요.")
    private DeliveryFailureReason reason;
}