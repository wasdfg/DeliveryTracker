package com.example.deliverytracker.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeliveryAddressUpdateRequest {

    @NotBlank(message = "배송지 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "수령인 이름을 입력해주세요.")
    private String receiverName;

    @NotBlank(message = "수령인 연락처를 입력해주세요.")
    private String receiverPhone;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    private String detailAddress;

    private Double latitude;

    private Double longitude;

    private boolean defaultAddress;
}
