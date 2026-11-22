package com.example.deliverytracker.payment.service;

import com.example.deliverytracker.coupon.service.CouponService;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.repository.OrderRepository;
import com.example.deliverytracker.order.service.OrderService;
import com.example.deliverytracker.payment.dto.PaymentApprovalRequest;
import com.example.deliverytracker.payment.dto.PaymentApprovalResult;
import com.example.deliverytracker.payment.dto.PaymentRefundResult;
import com.example.deliverytracker.payment.entity.Payment;
import com.example.deliverytracker.payment.entity.PaymentStatus;
import com.example.deliverytracker.payment.repository.PaymentRepository;
import com.example.deliverytracker.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentTransactionService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    private final OrderService orderService;
    private final CouponService couponService;


    // 승인 요청 시작
    @Transactional
    public Payment startApproval(Long paymentId, PaymentApprovalRequest request, User user) {

        validateUser(user);

        LockedPayment locked = lockForUser(paymentId, user);

        Order order = locked.order();

        Payment payment = locked.payment();

        if (payment.getStatus() != PaymentStatus.READY) {
            throw new IllegalStateException("결제 대기 상태에서만 승인할 수 있습니다.");
        }

        if (order.getStatus() != Order.Status.PENDING) {
            throw new IllegalStateException("결제 대기 상태의 주문만 접수할 수 있습니다.");
        }

        if (!Objects.equals(payment.getGatewayOrderId(), request.orderId())) {
            throw new IllegalArgumentException("결제 주문번호가 일치하지 않습니다.");
        }

        if (request.amount() == null || request.amount().compareTo(payment.getAmount()) != 0) {
            throw new IllegalArgumentException("요청 결제 금액이 주문 금액과 일치하지 않습니다.");
        }

        payment.startApproval(request.paymentKey());

        return payment;
    }


    // 승인 성공 결과 반영
    @Transactional
    public Payment completeApproval(Long paymentId, PaymentApprovalResult result, User user) {

        validateUser(user);

        LockedPayment locked = lockForUser(paymentId, user);

        Order order = locked.order();
        Payment payment = locked.payment();

        validateApprovalResult(payment, result);

        if (payment.getStatus() == PaymentStatus.APPROVED) {

            if (order.getStatus() != Order.Status.ACCEPTED) {
                throw new IllegalStateException("결제 상태와 주문 상태가 일치하지 않습니다.");
            }

            return payment;
        }

        if (order.getStatus() != Order.Status.PENDING) {
            throw new IllegalStateException("결제 대기 상태의 주문만 승인할 수 있습니다.");
        }

        payment.approve(result.paymentKey());

        if (order.getUserCoupon() != null) {
            couponService.confirmCouponUsage(order.getUser().getId(), order.getUserCoupon().getId(), order.getId());
        }

        orderService.acceptPaidOrder(order.getId(), user);

        return payment;
    }

    // PG 승인 실패 확정 결과 반영
    @Transactional
    public Payment failApproval(Long paymentId, String failureReason, User user) {

        validateUser(user);

        LockedPayment locked = lockForUser(paymentId, user);

        Payment payment = locked.payment();

        payment.fail(failureReason);

        return payment;
    }

    // PG 승인 결과 불확실
    @Transactional
    public Payment markApprovalUnknown(Long paymentId, String reason, User user) {

        validateUser(user);

        LockedPayment locked = lockForUser(paymentId, user);

        Payment payment = locked.payment();

        payment.markApprovalUnknown(reason);

        return payment;
    }

    // 승인 전 결제 취소
    @Transactional
    public Payment cancelPayment(Long paymentId, User user) {

        validateUser(user);

        LockedPayment locked = lockForUser(paymentId, user);

        Payment payment = locked.payment();

        if (payment.getStatus() != PaymentStatus.READY) {
            throw new IllegalStateException("결제 대기 상태에서만 취소할 수 있습니다.");
        }

        payment.cancel();

        return payment;
    }


    // 주문 취소에 따른 결제 시도 취소
    @Transactional(propagation = Propagation.MANDATORY)
    public Payment cancelPaymentForOrder(Long paymentId, Long orderId) {

        LockedPayment locked = lockForOrder(paymentId, orderId);

        Payment payment = locked.payment();

        if (payment.getStatus() != PaymentStatus.READY) {
            throw new IllegalStateException("결제 대기 상태에서만 취소할 수 있습니다.");
        }

        payment.cancel();

        return payment;
    }

    // 환불 요청 시작
    @Transactional
    public Payment startRefund(Long paymentId, User user) {

        validateUser(user);

        LockedPayment locked = lockForUser(paymentId, user);

        Payment payment = locked.payment();

        // APPROVED -> REFUNDING
        payment.startRefund();

        return payment;
    }


    // 환불 성공 결과 반영
    @Transactional
    public Payment completeRefund(Long paymentId, PaymentRefundResult result, User user) {

        validateUser(user);

        LockedPayment locked = lockForUser(paymentId, user);

        Payment payment = locked.payment();

        validateRefundResult(payment, result);

        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            return payment;
        }

        payment.refund();

        return payment;
    }


    // 환불 실패 확정 결과 반영
    @Transactional
    public Payment failRefund(Long paymentId, String failureReason, User user) {

        validateUser(user);

        LockedPayment locked = lockForUser(paymentId, user);

        Payment payment = locked.payment();

        payment.failRefund(failureReason);

        return payment;
    }


    // 환불 결과 불확실
    @Transactional
    public Payment markRefundUnknown(Long paymentId, String reason, User user) {

        validateUser(user);

        LockedPayment locked = lockForUser(paymentId, user);

        Payment payment = locked.payment();

        payment.markRefundUnknown(reason);

        return payment;
    }

    // 주문에 연결된 결제의 환불 시작
    @Transactional
    public Payment startRefundForOrder(Long paymentId, Long orderId) {

        LockedPayment locked = lockForOrder(paymentId, orderId);

        Payment payment = locked.payment();

        payment.startRefund();

        return payment;
    }

    // 주문에 연결된 결제의 환불 성공 결과 반영
    @Transactional
    public Payment completeRefundForOrder(Long paymentId, Long orderId, PaymentRefundResult result) {

        LockedPayment locked = lockForOrder(paymentId, orderId);

        Payment payment = locked.payment();

        validateRefundResult(payment, result);

        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            return payment;
        }

        payment.refund();

        return payment;
    }


    // 주문에 연결된 결제의 환불 실패 확정
    @Transactional
    public Payment failRefundForOrder(Long paymentId, Long orderId, String failureReason) {

        LockedPayment locked = lockForOrder(paymentId, orderId);

        Payment payment = locked.payment();

        payment.failRefund(failureReason);

        return payment;
    }


    // 주문에 연결된 결제의 환불 결과 불확실
    @Transactional
    public Payment markRefundUnknownForOrder(Long paymentId, Long orderId, String reason) {

        LockedPayment locked = lockForOrder(paymentId, orderId);

        Payment payment = locked.payment();

        payment.markRefundUnknown(reason);

        return payment;
    }


    // 사용자 검증
    private void validateUser(User user) {

        if (user == null || user.getRole() != User.Role.USER) {
            throw new IllegalArgumentException("일반 사용자만 결제할 수 있습니다.");
        }
    }


    // 주문 -> 결제 순서로 비관적 락 획득
    private LockedPayment lockForUser(Long paymentId, User user) {

        Order order = orderRepository.findByPaymentIdAndUserIdForUpdate(paymentId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("결제 중인 주문을 찾을 수 없습니다."));

        Payment payment = paymentRepository.findByIdAndUserIdForUpdate(paymentId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("결제 내용을 찾을 수 없습니다."));

        return new LockedPayment(order, payment);
    }


    // 주문 ID -> 결제 ID 순서로 비관적 락 획득
    private LockedPayment lockForOrder(Long paymentId, Long orderId) {

        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문 정보를 찾을 수 없습니다."));

        Payment payment = paymentRepository.findByIdAndOrderIdForUpdate(paymentId, orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문에 연결된 결제 정보를 찾을 수 없습니다."));

        return new LockedPayment(order, payment);
    }


    private void validateApprovalResult(Payment payment, PaymentApprovalResult result) {

        if (result == null || !result.success()) {
            throw new IllegalArgumentException("유효한 PG 승인 결과가 아닙니다.");
        }

        if (!Objects.equals(payment.getGatewayOrderId(), result.orderId())) {
            throw new IllegalStateException("PG 승인 결과의 주문번호가 일치하지 않습니다.");
        }

        if (result.approvedAmount() == null || result.approvedAmount().compareTo(payment.getAmount()) != 0) {
            throw new IllegalStateException("PG 승인 금액이 결제 금액과 일치하지 않습니다.");
        }

        if (!Objects.equals(payment.getPaymentKey(), result.paymentKey())) {
            throw new IllegalStateException("PG 결제 키가 일치하지 않습니다.");
        }
    }


    private void validateRefundResult(Payment payment, PaymentRefundResult result) {

        if (result == null || !result.success()) {
            throw new IllegalArgumentException("유효한 PG 환불 결과가 아닙니다.");
        }

        if (!Objects.equals(payment.getPaymentKey(), result.paymentKey())) {
            throw new IllegalStateException("환불 대상 결제 키가 일치하지 않습니다.");
        }

        if (result.refundedAmount() == null || result.refundedAmount().compareTo(payment.getAmount()) != 0) {
            throw new IllegalStateException("환불 금액이 결제 금액과 일치하지 않습니다.");
        }
    }


    private record LockedPayment(Order order, Payment payment) {
    }
}
