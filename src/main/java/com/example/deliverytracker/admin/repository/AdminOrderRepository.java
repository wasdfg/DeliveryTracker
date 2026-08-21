package com.example.deliverytracker.admin.repository;

import com.example.deliverytracker.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminOrderRepository extends JpaRepository<Order, Long>, AdminOrderRepositoryCustom{


}
