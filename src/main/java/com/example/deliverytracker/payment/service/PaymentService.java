package com.example.deliverytracker.payment.service;

import com.example.deliverytracker.coupon.entity.UserCoupon;
import com.example.deliverytracker.coupon.service.CouponService;
import com.example.deliverytracker.order.dto.OrderCreateRequest;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.repository.OrderRepository;
import com.example.deliverytracker.order.service.OrderService;
import com.example.deliverytracker.payment.dto.PaymentApprovalRequest;
import com.example.deliverytracker.payment.dto.PaymentApprovalResult;
import com.example.deliverytracker.payment.entity.Payment;
import com.example.deliverytracker.payment.entity.PaymentStatus;
import com.example.deliverytracker.payment.repository.PaymentRepository;
import com.example.deliverytracker.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final OrderService orderService;

    private final CouponService couponService;

    private final PaymentGateway paymentGateway;

    private final OrderRepository orderRepository;

    @Transactional
    public Payment createCheckout(OrderCreateRequest request, User user, Long userCouponId) {

        if(!user.getRole().equals(User.Role.USER)){
            throw new EntityNotFoundException("일반 사용자만 결제할 수 있습니다.");
        }

        Order order = orderService.createPendingOrder(request, user, userCouponId);

        Payment payment = Payment.create(order, order.getTotalPrice(), request.getPaymentMethod());

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment approvePayment(Long paymentId, PaymentApprovalRequest request, User user) {

        Payment payment = paymentRepository.findByIdAndUserIdForUpdate(paymentId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("결제 내용을 찾을 수 없습니다."));

        if (payment.getStatus() != PaymentStatus.READY) {
            throw new IllegalStateException("결제 대기 상태에서만 승인할 수 있습니다.");
        }

        Order order = payment.getOrder();

        if (order.getStatus() != Order.Status.PENDING) {
            throw new IllegalStateException("결제 대기 상태의 주문만 접수할 수 있습니다.");
        }

        if (!payment.getGatewayOrderId().equals(request.orderId())) {
            throw new IllegalArgumentException("결제 주문번호가 일치하지 않습니다.");
        }

        if (request.amount() == null || request.amount().compareTo(payment.getAmount()) != 0) {
            throw new IllegalArgumentException("요청 결제 금액이 주문 금액과 일치하지 않습니다.");
        }

        PaymentApprovalResult result = paymentGateway.approve(payment, request.paymentKey());

        if (!result.success()) {
            payment.fail(result.failureReason());

            return payment;
        }

        if (!payment.getGatewayOrderId().equals(result.orderId()) || result.approvedAmount().compareTo(payment.getAmount()) != 0) {
            throw new IllegalStateException("PG 승인 결과가 주문 정보와 일치하지 않습니다.");
        }

        payment.approve(result.paymentKey());

        if (order.getUserCoupon() != null) {
            couponService.confirmCouponUsage(user.getId(), order.getUserCoupon().getId(), order.getId());
        }

        orderService.acceptPaidOrder(order.getId(), user);

        return payment;
    }

    //결제 실패 처리
    @Transactional
    public Payment failPayment(Long paymentId, String failureReason, User user) {

        Payment payment = paymentRepository.findByIdAndUserIdForUpdate(paymentId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("결제 내용을 찾을 수 없습니다."));

        if (payment.getStatus() != PaymentStatus.READY) {
            throw new IllegalStateException("결제 대기 상태에서만 승인할 수 있습니다.");
        }

        payment.fail(failureReason);

        return payment;
    }

    // 결제 승인 전 취소 처리
    @Transactional
    public Payment cancelPayment(Long paymentId, User user) {

        Payment payment = paymentRepository.findByIdAndUserIdForUpdate(paymentId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("결제 내용을 찾을 수 없습니다."));

        if (payment.getStatus() != PaymentStatus.READY) {
            throw new IllegalStateException("결제 대기 상태에서만 취소할 수 있습니다.");
        }

        payment.cancel();

        return payment;
    }

    //기존 주문의 결제 재시도
    @Transactional
    public Payment retryPayment(Long orderId, User user) {

        if (user.getRole() != User.Role.USER) {
            throw new EntityNotFoundException("일반 사용자만 재결제할 수 있습니다.");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문 정보를 찾을 수 없습니다."));

        if(!order.getUser().equals(user)){
            throw new IllegalArgumentException("주문한 사용자만 결제 가능합니다.");
        }

        if (order.getStatus() != Order.Status.PENDING) {
            throw new IllegalStateException("결제 대기 상태의 주문만 재결제할 수 있습니다.");
        }

        if (paymentRepository.existsByOrder_IdAndStatus(orderId, PaymentStatus.APPROVED)) {
            throw new IllegalStateException("이미 결제가 완료된 주문입니다.");
        }

        if (paymentRepository.existsByOrder_IdAndStatus(orderId, PaymentStatus.READY)) {
            throw new IllegalStateException("이미 진행 중인 결제가 있습니다.");
        }

        Payment previousPayment = paymentRepository.findFirstByOrder_IdOrderByCreatedAtDescIdDesc(orderId)
                .orElseThrow(() -> new IllegalStateException("재결제할 기존 결제 내역이 없습니다."));

        if (previousPayment.getStatus() != PaymentStatus.FAILED && previousPayment.getStatus() != PaymentStatus.CANCELED) {
            throw new IllegalStateException("실패하거나 취소된 결제만 재시도할 수 있습니다.");
        }

        if (order.getUserCoupon() != null) {
            UserCoupon userCoupon = order.getUserCoupon();

            if (userCoupon.isUsed() || !Objects.equals(userCoupon.getReservedOrderId(), order.getId())) {
                throw new IllegalStateException("주문에 적용된 쿠폰 예약을 확인할 수 없습니다.");
            }
        }

        Payment newPayment = Payment.create(order, order.getTotalPrice(), previousPayment.getMethod());

        return paymentRepository.save(newPayment);
    }

    // 승인된 결제 환불 처리
    @Transactional
    public Payment refundPayment(Long paymentId, User user) {
        return null; // TODO
    }
}