package com.example.deliverytracker.order.repository;

import com.example.deliverytracker.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
