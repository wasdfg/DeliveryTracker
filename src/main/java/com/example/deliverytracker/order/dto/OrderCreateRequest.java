package com.example.deliverytracker.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequest {

    @NotBlank
    private String nickname;

    @NotBlank
    private String pickupAddress;

    @NotBlank
    private String deliveryAddress;

    private List<Item> items;

    @Getter
    public static class Item {
        private String name;
        private int quantity;
        private int price;
        private String imageUrl;
    }
}
