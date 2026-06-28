package com.example.deliverytracker.store.dto;

import lombok.Getter;

@Getter
public class StoreStatusRequest {

    private Boolean active;

    private Boolean deleted;
}