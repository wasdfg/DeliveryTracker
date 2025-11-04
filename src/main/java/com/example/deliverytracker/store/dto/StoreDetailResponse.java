package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.Store;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class StoreDetailResponse {

    private Long id;

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

    private String averageRating;

    private int reviewCount;

    public StoreDetailResponse(Store store, List<ProductResponse> productResponses) {
        this.id = store.getId();
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
        this.averageRating = String.format("%.1f", store.getAverageRating());
        this.reviewCount = store.getReviewCount();

    }

    public static StoreDetailResponse from(Store store) {
        List<ProductResponse> productResponses = store.getProducts().stream()
                .map(ProductResponse::from)
                .toList();

        return new StoreDetailResponse(store, productResponses);
    }
}
