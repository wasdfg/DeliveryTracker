package com.example.deliverytracker.address.dto;

import com.example.deliverytracker.address.enitity.Address;
import lombok.Getter;

@Getter
public class AddressResponseDto {

    private Long id;
    private String alias;
    private String streetAddress;
    private String detailAddress;

    public AddressResponseDto(Address address) {
        this.id = address.getId();
        this.alias = address.getAlias();
        this.streetAddress = address.getStreetAddress();
        this.detailAddress = address.getDetailAddress();
    }
}
