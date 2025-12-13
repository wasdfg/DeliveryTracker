package com.example.deliverytracker.coupon.service;

import com.example.deliverytracker.coupon.dto.CouponResponseDto;
import com.example.deliverytracker.coupon.entity.UserCoupon;
import com.example.deliverytracker.coupon.repository.UserCouponRepository;
import com.example.deliverytracker.user.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final UserCouponRepository userCouponRepository;

    @Transactional(readOnly = true)
    public List<CouponResponseDto> getMyCoupons(User user) {
        return userCouponRepository.findAllAvailableByUserId(user.getId()).stream()
                .map(CouponResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public int validateAndUseCoupon(Long userCouponId, User user, int totalOrderPrice) {

        UserCoupon userCoupon = userCouponRepository.findByIdWithCouponAndUser(userCouponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

        if (!userCoupon.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("회원님의 쿠폰이 아닙니다.");
        }

        if (userCoupon.isUsed()) {
            throw new IllegalArgumentException("이미 사용된 쿠폰입니다.");
        }

        if (totalOrderPrice < userCoupon.getCoupon().getMinOrderAmount()) {
            throw new IllegalArgumentException("최소 주문 금액을 만족하지 못했습니다.");
        }

        userCoupon.use();

        return userCoupon.getCoupon().getDiscountAmount();
    }
}
