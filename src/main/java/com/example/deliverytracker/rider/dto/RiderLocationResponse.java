package com.example.deliverytracker.rider.dto;

import lombok.Getter;

@Getter
public class RiderLocationResponse {

    private Double latitude;

    private Double longitude;

    public RiderLocationResponse(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
