package com.example.deliverytracker.coupon.repository;

import com.example.deliverytracker.coupon.entity.Coupon;
import com.example.deliverytracker.coupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code);
}
