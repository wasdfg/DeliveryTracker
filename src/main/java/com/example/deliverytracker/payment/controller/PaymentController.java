package com.example.deliverytracker.payment.controller;

import com.example.deliverytracker.order.dto.OrderCreateRequest;
import com.example.deliverytracker.payment.dto.PaymentApprovalRequest;
import com.example.deliverytracker.payment.dto.PaymentResponse;
import com.example.deliverytracker.payment.entity.Payment;
import com.example.deliverytracker.payment.service.PaymentService;
import com.example.deliverytracker.user.entity.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/checkout")
    public ResponseEntity<PaymentResponse> createCheckout(@Valid @RequestBody OrderCreateRequest request, @RequestParam(required = false) Long userCouponId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Payment payment = paymentService.createCheckout(request, userDetails.getUser(), userCouponId);

        return ResponseEntity.status(HttpStatus.CREATED).body(PaymentResponse.from(payment));
    }

    @PostMapping("/{paymentId}/approve")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaymentResponse> approve(@PathVariable Long paymentId, @Valid @RequestBody PaymentApprovalRequest paymentApprovalRequest, @AuthenticationPrincipal UserDetailsImpl userDetails){

        Payment payment = paymentService.approvePayment(paymentId, paymentApprovalRequest,userDetails.getUser());

        return ResponseEntity.ok(PaymentResponse.from(payment));
    }

    @PostMapping("/{paymentId}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaymentResponse> cancel(@PathVariable Long paymentId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        Payment payment = paymentService.cancelPayment(paymentId,userDetails.getUser());

        return ResponseEntity.ok(PaymentResponse.from(payment));
    }

    @PostMapping("/orders/{orderId}/retry")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaymentResponse> retry(@PathVariable Long orderId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        Payment payment = paymentService.retryPayment(orderId,userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(PaymentResponse.from(payment));
    }

}