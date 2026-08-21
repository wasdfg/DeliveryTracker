package com.example.deliverytracker.admin.repository;

import com.example.deliverytracker.admin.dto.AdminOrderSearchCondition;
import com.example.deliverytracker.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AdminOrderRepositoryCustom {

    Page<Order> searchOrders(AdminOrderSearchCondition condition, Pageable pageable);

    Optional<Order> findAdminOrder(Long orderId);
}
