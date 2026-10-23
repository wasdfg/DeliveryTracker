package com.example.deliverytracker.payment.service;

import com.example.deliverytracker.order.dto.OrderCreateRequest;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.service.OrderService;
import com.example.deliverytracker.payment.entity.Payment;
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
        return null; // TODO
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