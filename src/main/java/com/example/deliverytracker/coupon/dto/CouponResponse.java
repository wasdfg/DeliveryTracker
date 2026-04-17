package com.example.deliverytracker.coupon.dto;

import com.example.deliverytracker.coupon.entity.Coupon;

public record CouponResponse(
        Long id,
        String name,
        int discountAmount,
        int totalQuantity,
        int issuedQuantity
) {
    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountAmount(),
                coupon.getTotalQuantity(),
                coupon.getIssuedQuantity()
        );
    }
}
