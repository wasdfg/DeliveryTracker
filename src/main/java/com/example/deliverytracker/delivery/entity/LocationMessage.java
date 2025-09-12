package com.example.deliverytracker.delivery.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationMessage {
    private Long orderId;
    private Long riderId;
    private double latitude;
    private double longitude;
}
