package com.example.deliverytracker.admin.controller;

import com.example.deliverytracker.admin.dto.AdminOrderDetailResponse;
import com.example.deliverytracker.admin.dto.AdminOrderResponse;
import com.example.deliverytracker.admin.dto.AdminOrderSearchCondition;
import com.example.deliverytracker.admin.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    public ResponseEntity<Page<AdminOrderResponse>> getOrders(AdminOrderSearchCondition condition, Pageable pageable) {
        return ResponseEntity.ok(adminOrderService.getOrders(condition, pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<AdminOrderDetailResponse> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(adminOrderService.getOrder(orderId));
    }
}
