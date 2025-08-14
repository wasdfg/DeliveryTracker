package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.Store;
import lombok.Getter;

import java.util.List;

@Getter
public class StoreDetailResponse {

    private String storeName;

    private String storePhone;

    private String storeAddress;

    private List<ProductResponse> products;

    public StoreDetailResponse(String name, String phone, String address, List<ProductResponse> productResponses) {
        this.storeName = name;
        this.storePhone = phone;
        this.storeAddress = address;
        this.products = productResponses;
    }

    public static StoreDetailResponse from(Store store) {
        List<ProductResponse> productResponses = store.getProducts().stream()
                .map(ProductResponse::from)
                .toList();

        return new StoreDetailResponse(
                store.getName(),
                store.getPhone(),
                store.getAddress(),
                productResponses
        );
    }
}
