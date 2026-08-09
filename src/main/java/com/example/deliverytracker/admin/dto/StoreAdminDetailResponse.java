package com.example.deliverytracker.admin.dto;

import com.example.deliverytracker.store.entity.Store;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class StoreAdminDetailResponse {

    private Long storeId;

    private String name;

    private Long ownerId;

    private String ownerNickname;

    private String phone;

    private String address;

    private String description;

    private String categoryName;

    private Boolean active;

    private Boolean deleted;

    private String operatingHours;

    private BigDecimal minOrderAmount;

    private Integer deliveryFee;

    private String imageUrl;

    private Double averageRating;

    private Integer reviewCount;

    private Integer productCount;

    private Long totalOrderCount;

    private Long totalSales;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long averageOrderPrice;

    private Long canceledOrderCount;

    private Double cancellationRate;

    public static StoreAdminDetailResponse from(Store store) {

        StoreAdminDetailResponse response = new StoreAdminDetailResponse();

        response.storeId = store.getId();
        response.name = store.getName();

        response.ownerId = store.getOwner().getId();
        response.ownerNickname = store.getOwner().getNickname();

        response.phone = store.getPhone();
        response.address = store.getAddress();
        response.description = store.getDescription();

        response.categoryName = store.getCategory().getName();

        response.active = store.isActive();
        response.deleted = store.isDeleted();

        response.minOrderAmount = store.getMinOrderAmount();
        response.deliveryFee = store.getDeliveryFee();

        response.imageUrl = store.getImageUrl();

        response.averageRating = store.getAverageRating();
        response.reviewCount = store.getReviewCount();
        response.productCount = store.getProducts().size();

        response.createdAt = store.getCreatedAt();
        response.updatedAt = store.getUpdatedAt();

        return response;
    }

    public void updateSummary(Long totalOrderCount, Long totalSales, Long averageOrderPrice, Long canceledOrderCount, Double cancellationRate) {
        this.totalOrderCount = totalOrderCount;
        this.totalSales = totalSales;
        this.averageOrderPrice = averageOrderPrice;
        this.canceledOrderCount = canceledOrderCount;
        this.cancellationRate = totalOrderCount == 0 ? 0 : (double) canceledOrderCount / totalOrderCount * 100;
    }
}
