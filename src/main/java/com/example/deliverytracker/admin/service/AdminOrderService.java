package com.example.deliverytracker.admin.service;

import com.example.deliverytracker.admin.dto.AdminOrderDetailResponse;
import com.example.deliverytracker.admin.dto.AdminOrderResponse;
import com.example.deliverytracker.admin.dto.AdminOrderSearchCondition;
import com.example.deliverytracker.admin.repository.AdminOrderRepository;
import com.example.deliverytracker.order.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final AdminOrderRepository adminOrderRepository;

    public Page<AdminOrderResponse> getOrders(AdminOrderSearchCondition condition, Pageable pageable){
        return adminOrderRepository.searchOrders(condition, pageable).map(AdminOrderResponse::from);
    }

    public AdminOrderDetailResponse getOrder(Long orderId){
        Order order = adminOrderRepository.findAdminOrder(orderId).orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        return AdminOrderDetailResponse.from(order);
    }
}
