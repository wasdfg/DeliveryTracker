package com.example.deliverytracker.rider.dto;

import com.example.deliverytracker.rider.entity.Rider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiderStatusRequestDto {
    private Rider.Status status;
}
