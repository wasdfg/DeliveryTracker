package com.example.deliverytracker.coupon.service;

import com.example.deliverytracker.coupon.dto.CouponResponse;
import com.example.deliverytracker.coupon.entity.Coupon;
import com.example.deliverytracker.coupon.entity.UserCoupon;
import com.example.deliverytracker.coupon.repository.CouponRepository;
import com.example.deliverytracker.coupon.repository.UserCouponRepository;
import com.example.deliverytracker.user.entitiy.User;
import com.example.deliverytracker.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final UserCouponRepository userCouponRepository;

    private final CouponRepository couponRepository;

    private final UserRepository userRepository;

    public List<CouponResponse> getMyCoupons(User user) {
        return userCouponRepository.findAllAvailableByUserId(user.getId()).stream()
                .map(userCoupon -> CouponResponse.from(userCoupon.getCoupon()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void issueCoupon(User user, Long couponId) {

        Coupon coupon = couponRepository.findByIdWithLock(couponId)
                .orElseThrow(() -> new EntityNotFoundException("쿠폰을 찾을 수 없습니다."));

        if (userCouponRepository.existsByUserAndCoupon(user, coupon)) {
            throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
        }

        coupon.issue();

        userCouponRepository.save(new UserCoupon(user, coupon));
    }

    @Transactional
    public void issueCouponByCode(User user, String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 쿠폰 코드입니다."));

        issueCoupon(user, coupon.getId());
    }

    public List<CouponResponse> getAllCoupons() {
        List<Coupon> publicCoupons = couponRepository.findByCodeIsNull();

        return publicCoupons.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public int useCoupon(Long userId, Long userCouponId) {
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new EntityNotFoundException("보유하지 않은 쿠폰입니다."));

        if (!userCoupon.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("본인의 쿠폰만 사용할 수 있습니다.");
        }
        if (userCoupon.isUsed()) {
            throw new IllegalStateException("이미 사용된 쿠폰입니다.");
        }

        userCoupon.use();

        return userCoupon.getCoupon().getDiscountAmount();
    }
}
