package com.example.deliverytracker.admin.dto;

import com.example.deliverytracker.store.dto.StoreResponse;
import com.example.deliverytracker.store.entity.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreAdminResponse {

    private Long storeId;

    private String name;

    private Boolean active;

    private Long ownerId;

    private Boolean deleted;

    public StoreAdminResponse(Long storeId, String name, Boolean active, Long ownerId, Boolean deleted) {
        this.storeId = storeId;
        this.name = name;
        this.active = active;
        this.ownerId = ownerId;
        this.deleted = deleted;
    }

    public static StoreAdminResponse from(Store store) {
        return new StoreAdminResponse(
                store.getId(),
                store.getName(),
                store.isActive(),
                store.getOwner().getId(),
                store.isDeleted()

        );
    }
}
