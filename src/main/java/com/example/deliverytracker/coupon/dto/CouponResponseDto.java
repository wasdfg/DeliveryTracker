package com.example.deliverytracker.coupon.dto;

import com.example.deliverytracker.coupon.entity.UserCoupon;
import lombok.Getter;

@Getter
public class CouponResponseDto {
    private Long id;            // UserCoupon의 ID (주문할 때 이 ID를 보냄)
    private String name;
    private int discountAmount;
    private int minOrderAmount;

    public CouponResponseDto(UserCoupon userCoupon) {
        this.id = userCoupon.getId();
        this.name = userCoupon.getCoupon().getName();
        this.discountAmount = userCoupon.getCoupon().getDiscountAmount();
        this.minOrderAmount = userCoupon.getCoupon().getMinOrderAmount();
    }
}
