package com.example.deliverytracker.order.service;

import com.example.deliverytracker.coupon.service.CouponService;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.repository.OrderRepository;
import com.example.deliverytracker.payment.entity.Payment;
import com.example.deliverytracker.payment.repository.PaymentRepository;
import com.example.deliverytracker.payment.service.PaymentService;
import com.example.deliverytracker.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderCancellationService {

    private final OrderRepository orderRepository;

    private final PaymentRepository paymentRepository;

    private final OrderService orderService;

    private final PaymentService paymentService;

    private final CouponService couponService;

    @Transactional
    public Order cancelOrder(Long orderId, String cancelReason, User user) {

        Order order = orderRepository.findByIdAndUserIdForUpdate(orderId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("주문 정보를 찾을 수 없습니다."));

        if (order.getStatus() != Order.Status.PENDING && order.getStatus() != Order.Status.ACCEPTED) {
            throw new IllegalStateException("현재 주문 상태에서는 취소할 수 없습니다.");
        }

        Payment payment = paymentRepository.findFirstByOrder_IdOrderByCreatedAtDescIdDesc(orderId).orElse(null);

        if (payment != null) {
            switch (payment.getStatus()) {
                case READY ->
                        paymentService.cancelPayment(payment.getId(), user);

                case APPROVED ->
                        paymentService.refundPayment(payment.getId(), cancelReason, user);

                case FAILED, CANCELED, REFUNDED -> {
                }
            }
        }

        if (order.getUserCoupon() != null && !order.getUserCoupon().isUsed()) {

            couponService.releaseCouponReservation(user.getId(), order.getUserCoupon().getId(), order.getId());
        }

        return orderService.cancelOrderAfterPayment(orderId, user);
    }
}
