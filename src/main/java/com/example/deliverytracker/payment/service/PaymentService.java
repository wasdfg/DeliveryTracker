package com.example.deliverytracker.payment.service;

import com.example.deliverytracker.coupon.service.CouponService;
import com.example.deliverytracker.order.dto.OrderCreateRequest;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.service.OrderService;
import com.example.deliverytracker.payment.entity.Payment;
import com.example.deliverytracker.payment.entity.PaymentStatus;
import com.example.deliverytracker.payment.repository.PaymentRepository;
import com.example.deliverytracker.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final OrderService orderService;

    private final CouponService couponService;

    @Transactional
    public Payment createCheckout(OrderCreateRequest request, User user, Long userCouponId) {

        if(!user.getRole().equals(User.Role.USER)){
            throw new EntityNotFoundException("일반 사용자만 결제할 수 있습니다.");
        }

        Order order = orderService.createPendingOrder(request, user, userCouponId);

        Payment payment = Payment.create(order, order.getTotalPrice(), request.getPaymentMethod());

        return paymentRepository.save(payment);
    }

    // 2. 결제 승인 처리
    @Transactional
    public Payment approvePayment(Long paymentId, User user) {

        Payment payment = paymentRepository.findByIdAndOrder_User_Id(paymentId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("결제 내용을 찾을 수 없습니다."));

        if (payment.getStatus() != PaymentStatus.READY) {
            throw new IllegalStateException("결제 대기 상태에서만 승인할 수 있습니다.");
        }

        Order order = payment.getOrder();

        if (order.getStatus() != Order.Status.PENDING) {
            throw new IllegalStateException("결제 대기 상태의 주문만 접수할 수 있습니다.");
        }

        // 4. 실제 PG 또는 Mock 결제 승인 처리
        // 승인 성공 여부와 결제 금액을 검증하고 paymentKey를 받아야 함
        // String paymentKey = ...

        // 5. 결제 승인 상태 변경
        // payment.approve(paymentKey);

        if (order.getUserCoupon() != null) {
            couponService.confirmCouponUsage(user.getId(), order.getUserCoupon().getId(), order.getId());
        }

        orderService.acceptPaidOrder(order.getId(), user);

        return payment;
    }

    // 3. 결제 실패 처리
    @Transactional
    public Payment failPayment(Long paymentId, String failureReason, User user) {
        return null; // TODO
    }

    // 4. 결제 승인 전 취소 처리
    @Transactional
    public Payment cancelPayment(Long paymentId, User user) {
        return null; // TODO
    }

    // 5. 기존 주문의 결제 재시도
    @Transactional
    public Payment retryPayment(Long orderId, User user) {
        return null; // TODO
    }

    // 6. 승인된 결제 환불 처리
    @Transactional
    public Payment refundPayment(Long paymentId, User user) {
        return null; // TODO
    }
}