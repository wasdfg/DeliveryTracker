package com.example.deliverytracker.order.repository;

import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.user.entitiy.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.store s " +
            "JOIN FETCH o.orderItems oi " +
            "JOIN FETCH oi.product p " +
            "WHERE o.id = :orderId")
    Optional<Order> findOrderWithDetailsById(@Param("orderId") Long orderId);

    Page<Order> findByUserAndStatus(User user, Order.Status status, Pageable pageable);

    Page<Order> findByUser(User user, Pageable pageable);

    Page<Order> findByStore(Store store, Pageable pageable);

    Page<Order> findAllByUserOrderByRequestedAtDesc(User user, Pageable pageable);

    Page<Order> findAllByUserAndStatusOrderByRequestedAtDesc(User user, Order.Status status, Pageable pageable);
}
