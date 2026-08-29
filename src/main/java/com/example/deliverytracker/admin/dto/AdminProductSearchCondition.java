package com.example.deliverytracker.admin.dto;

import lombok.Getter;

@Getter
public class AdminProductSearchCondition {

    private String productName;

    private String storeName;

    private Boolean active;

    private Boolean deleted;
}
