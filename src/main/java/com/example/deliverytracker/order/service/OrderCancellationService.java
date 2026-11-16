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
import org.springframework.security.access.AccessDeniedException;
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

        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문 정보를 찾을 수 없습니다."));

        boolean isCustomer = user.getRole() == User.Role.USER && order.getUser().getId().equals(user.getId());

        boolean isStoreOwner = user.getRole() == User.Role.STORE_OWNER && order.getStore().getOwner().getId().equals(user.getId());

        if (!isCustomer && !isStoreOwner) {
            throw new AccessDeniedException("해당 주문을 취소할 권한이 없습니다.");
        }

        if (order.getStatus() != Order.Status.PENDING && order.getStatus() != Order.Status.ACCEPTED) {
            throw new IllegalStateException("현재 주문 상태에서는 취소할 수 없습니다.");
        }

        Payment payment = paymentRepository.findFirstByOrder_IdOrderByCreatedAtDescIdDesc(orderId).orElse(null);

        if (payment != null) {
            switch (payment.getStatus()) {
                case READY ->
                        paymentService.cancelPaymentForOrder(payment.getId(), orderId);

                case APPROVED ->
                        paymentService.refundPaymentForOrder(payment.getId(), orderId, cancelReason);

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
