package com.example.deliverytracker.coupon.contorller;

import com.example.deliverytracker.coupon.dto.CouponCodeRequest;
import com.example.deliverytracker.coupon.dto.CouponResponse;
import com.example.deliverytracker.coupon.service.CouponService;
import com.example.deliverytracker.user.entitiy.User;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/my")
    public ResponseEntity<List<CouponResponse>> getMyCoupons(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CouponResponse> coupons = couponService.getMyCoupons(userDetails.getUser());
        return ResponseEntity.ok(coupons);
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> getAvailableCoupons() {
        List<CouponResponse> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @PostMapping("/{couponId}/issue")
    public ResponseEntity<Void> issueCoupon(@PathVariable Long couponId, User user) {

        couponService.issueCoupon(user, couponId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/issue-by-code")
    public ResponseEntity<Void> issueCouponByCode(@RequestBody CouponCodeRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        couponService.issueCouponByCode(userDetails.getUser(), request.code());
        return ResponseEntity.ok().build();
    }
}
