package com.example.deliverytracker.rider.dto;

import com.example.deliverytracker.rider.entity.Rider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiderStatusRequest {
    private Rider.Status status;
}
