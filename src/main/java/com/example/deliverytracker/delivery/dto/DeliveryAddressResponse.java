package com.example.deliverytracker.delivery.dto;

import com.example.deliverytracker.delivery.entity.DeliveryAddress;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryAddressResponse {

    private Long id;
    private String name;
    private String receiverName;
    private String receiverPhone;
    private String address;
    private String detailAddress;
    private Double latitude;
    private Double longitude;
    private boolean defaultAddress;

    public static DeliveryAddressResponse from(DeliveryAddress address) {
        return DeliveryAddressResponse.builder()
                .id(address.getId())
                .name(address.getName())
                .receiverName(address.getReceiverName())
                .receiverPhone(address.getReceiverPhone())
                .address(address.getAddress())
                .detailAddress(address.getDetailAddress())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .defaultAddress(address.isDefaultAddress())
                .build();
    }
}
