package com.example.deliverytracker.admin.controller;

import com.example.deliverytracker.admin.dto.AdminOrderDetailResponse;
import com.example.deliverytracker.admin.dto.AdminOrderReasonRequest;
import com.example.deliverytracker.admin.dto.AdminOrderResponse;
import com.example.deliverytracker.admin.dto.AdminOrderSearchCondition;
import com.example.deliverytracker.admin.dto.AdminOrderStatusRequest;
import com.example.deliverytracker.admin.service.AdminOrderService;
import com.example.deliverytracker.user.entity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/admin/orders")
@RestController
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<AdminOrderResponse>> getOrders(AdminOrderSearchCondition condition, Pageable pageable) {
        return ResponseEntity.ok(adminOrderService.getOrders(condition, pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{orderId}")
    public ResponseEntity<AdminOrderDetailResponse> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(adminOrderService.getOrder(orderId));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateAdminOrderStatus(@PathVariable Long orderId, @RequestBody AdminOrderStatusRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        adminOrderService.updateAdminOrderStatus(orderId, request.getStatus(), request.getReason(), userDetails.getUser());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId, @RequestBody AdminOrderReasonRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        adminOrderService.cancelOrder(orderId, request.getReason(), userDetails.getUser());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId, @RequestBody AdminOrderReasonRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        adminOrderService.deleteOrder(orderId, request.getReason(), userDetails.getUser());

        return ResponseEntity.ok().build();
    }
}
