package com.example.deliverytracker.delivery.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryRequest {
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private String itemDescription;
}
