package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.order.dto.OrderItemResponse;
import com.example.deliverytracker.order.entity.OrderItem;
import com.example.deliverytracker.store.entity.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreResponse {

    private Long storeId;

    private String name;

    public StoreResponse(Long storeId, String name) {
        this.storeId = storeId;
        this.name = name;
    }

    public static StoreResponse from(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getName()
        );
    }
}
