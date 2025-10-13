package com.example.deliverytracker.store.entity;

import lombok.Getter;

@Getter
public enum DeliveryTime {
    TEN_TO_TWENTY("10-20분"),
    TWENTY_TO_THIRTY("20-30분"),
    THIRTY_TO_FORTY("30-40분"),
    FORTY_TO_FIFTY("40-50분"),
    FIFTY_TO_SIXTY("50-60분");

    private final String description;

    DeliveryTime(String description) {
        this.description = description;
    }
}
