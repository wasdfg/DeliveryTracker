package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.Store;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class StoreDetailResponse {

    private String storeName;

    private String storePhone;

    private String storeAddress;

    private String description;

    private String operatingHours;

    private BigDecimal minOrderAmount;

    private int deliveryFee;

    private String imageUrl;

    private List<ProductResponse> products;

    public StoreDetailResponse(String name, String phone, String address, List<ProductResponse> productResponses,String description, String operatingHours, BigDecimal minOrderAmount, int deliveryFee,String imageUrl) {
        this.storeName = name;
        this.storePhone = phone;
        this.storeAddress = address;
        this.products = productResponses;
        this.description = description;
        this.operatingHours = operatingHours;
        this.minOrderAmount = minOrderAmount;
        this.deliveryFee = deliveryFee;
        this.imageUrl = imageUrl;
    }

    public static StoreDetailResponse from(Store store) {
        List<ProductResponse> productResponses = store.getProducts().stream()
                .map(ProductResponse::from)
                .toList();

        return new StoreDetailResponse(
                store.getName(),
                store.getPhone(),
                store.getAddress(),
                productResponses,
                store.getDescription(),
                store.getOperatingHours(),
                store.getMinOrderAmount(),
                store.getDeliveryFee(),
                store.getImageUrl()
        );
    }
}
