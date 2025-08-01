package com.example.deliverytracker.delivery.dto;

import com.example.deliverytracker.delivery.entity.Delivery;
import com.example.deliverytracker.delivery.entity.DeliveryStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryResponse {
    private Long id;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private String itemDescription;
    private DeliveryStatus status;

    public static DeliveryResponse from(Delivery delivery) {
        return DeliveryResponse.builder()
                .id(delivery.getId())
                .receiverName(delivery.getReceiverName())
                .receiverAddress(delivery.getReceiverAddress())
                .receiverPhone(delivery.getReceiverPhone())
                .itemDescription(delivery.getItemDescription())
                .status(delivery.getStatus())
                .build();
    }
}
