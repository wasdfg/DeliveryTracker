package com.example.deliverytracker.coupon.contorller;

import com.example.deliverytracker.coupon.dto.CouponResponseDto;
import com.example.deliverytracker.coupon.service.CouponService;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    // 내 쿠폰 목록 조회
    @GetMapping("/my")
    public ResponseEntity<List<CouponResponseDto>> getMyCoupons(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CouponResponseDto> coupons = couponService.getMyCoupons(userDetails.getUser());
        return ResponseEntity.ok(coupons);
    }
}
