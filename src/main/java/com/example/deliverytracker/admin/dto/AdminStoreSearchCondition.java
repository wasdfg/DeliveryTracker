package com.example.deliverytracker.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminStoreSearchCondition {

    private String keyword;

    private Long categoryId;

    private Boolean active;

    private Boolean deleted;

    private Long ownerId;
}
