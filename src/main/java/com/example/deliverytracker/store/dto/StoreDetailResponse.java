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

    private CategoryResponseDto category;

    private List<ProductResponse> products;

    public StoreDetailResponse(Store store, List<ProductResponse> productResponses) {
        this.storeName = store.getName();
        this.storePhone = store.getPhone();
        this.storeAddress = store.getAddress();
        this.products = productResponses;
        this.description = store.getDescription();
        this.operatingHours = store.getOperatingHours();
        this.minOrderAmount = store.getMinOrderAmount();
        this.deliveryFee = store.getDeliveryFee();
        this.imageUrl = store.getImageUrl();
        this.category = CategoryResponseDto.from(store.getCategory());
    }

    public static StoreDetailResponse from(Store store) {
        List<ProductResponse> productResponses = store.getProducts().stream()
                .map(ProductResponse::from)
                .toList();

        return new StoreDetailResponse(store, productResponses);
    }
}
