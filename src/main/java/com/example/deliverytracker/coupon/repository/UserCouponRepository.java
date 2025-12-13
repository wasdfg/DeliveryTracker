package com.example.deliverytracker.coupon.repository;

import com.example.deliverytracker.coupon.entity.UserCoupon;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {


    @Query("select uc from UserCoupon uc join fetch uc.coupon c where uc.user.id = :userId and uc.isUsed = false")
    List<UserCoupon> findAllAvailableByUserId(@Param("userId") Long userId);

    @Query("select uc from UserCoupon uc join fetch uc.coupon c join fetch uc.user u where uc.id = :userCouponId")
    Optional<UserCoupon> findByIdWithCouponAndUser(@Param("userCouponId") Long userCouponId);
}