package com.example.deliverytracker.order.dto;

import com.example.deliverytracker.coupon.entity.Coupon;
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

    private Coupon coupon;

    private List<Item> orderItems;

    @Getter
    public static class Item {
        private Long productId;
        private int quantity;
        private List<Long> optionIds;
    }
}
