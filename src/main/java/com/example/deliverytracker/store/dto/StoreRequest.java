package com.example.deliverytracker.store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class StoreRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String phone;
}
