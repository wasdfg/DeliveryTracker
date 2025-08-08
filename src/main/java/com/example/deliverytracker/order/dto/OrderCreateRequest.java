package com.example.deliverytracker.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

    @NotBlank
    private String nickname;

    @NotBlank
    private String pickupAddress;

    @NotBlank
    private String deliveryAddress;

    @NotNull
    private Long storeId;

    private List<Item> items;

    @Getter
    public static class Item {
        private Long productId;  // 기존 name → productId
        private int quantity;
    }
}
