package com.example.deliverytracker.address.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequestDto {

    @NotBlank(message = "주소 별칭은 필수입니다.")
    private String alias;

    @NotBlank(message = "도로명 주소는 필수입니다.")
    private String streetAddress;

    private String detailAddress;
}