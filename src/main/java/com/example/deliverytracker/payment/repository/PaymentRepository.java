package com.example.deliverytracker.payment.repository;


import com.example.deliverytracker.payment.entity.Payment;
import com.example.deliverytracker.payment.entity.PaymentStatus;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByIdAndOrder_User_Id(Long paymentId, Long userId);

    List<Payment> findAllByOrder_IdOrderByCreatedAtDesc(Long orderId);

    Optional<Payment> findFirstByOrder_IdAndStatusOrderByCreatedAtDesc(Long orderId, PaymentStatus status);

    boolean existsByOrder_IdAndStatus(Long orderId, PaymentStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT p
        FROM Payment p
        WHERE p.id = :paymentId
          AND p.order.user.id = :userId
        """)
    Optional<Payment> findByIdAndUserIdForUpdate(@Param("paymentId") Long paymentId, @Param("userId") Long userId);
}
