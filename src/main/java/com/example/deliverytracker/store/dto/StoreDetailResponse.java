package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.OperationTime;
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

    private CategoryResponse category;

    private List<ProductResponse> products;

    private String averageRating;

    private int reviewCount;

    private List<OperationTimeResponse> operationTimes;

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
        this.category = CategoryResponse.from(store.getCategory());
        this.averageRating = String.format("%.1f", store.getAverageRating());
        this.reviewCount = store.getReviewCount();
        this.operationTimes = store.getOperationTimes().stream()
                .map(OperationTimeResponse::from)
                .toList();
    }

    public static StoreDetailResponse from(Store store) {
        List<ProductResponse> productResponses = store.getProducts().stream()
                .map(ProductResponse::from)
                .toList();

        return new StoreDetailResponse(store, productResponses);
    }

    @Getter
    public static class OperationTimeResponse {
        private String dayOfWeek;
        private String openTime;
        private String closeTime;
        private boolean isDayOff;

        public static OperationTimeResponse from(OperationTime ot) {
            OperationTimeResponse res = new OperationTimeResponse();
            res.dayOfWeek = ot.getDayOfWeek().name();
            res.openTime = ot.getOpenTime().toString();
            res.closeTime = ot.getCloseTime().toString();
            res.isDayOff = ot.isDayOff();
            return res;
        }
    }
}
