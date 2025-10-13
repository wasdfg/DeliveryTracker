package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.DeliveryTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryTimeUpdateRequestDto {
    private DeliveryTime deliveryTime;
}
