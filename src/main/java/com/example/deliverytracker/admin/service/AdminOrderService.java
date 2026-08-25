package com.example.deliverytracker.admin.service;

import com.example.deliverytracker.admin.dto.AdminOrderDetailResponse;
import com.example.deliverytracker.admin.dto.AdminOrderResponse;
import com.example.deliverytracker.admin.dto.AdminOrderSearchCondition;
import com.example.deliverytracker.admin.entity.AdminAction;
import com.example.deliverytracker.admin.entity.TargetType;
import com.example.deliverytracker.admin.repository.AdminOrderRepository;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.repository.OrderRepository;
import com.example.deliverytracker.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    private final OrderRepository orderRepository;

    private final AdminLogService adminLogService;

    public Page<AdminOrderResponse> getOrders(AdminOrderSearchCondition condition, Pageable pageable){
        return adminOrderRepository.searchOrders(condition, pageable).map(AdminOrderResponse::from);
    }

    public AdminOrderDetailResponse getOrder(Long orderId){
        Order order = adminOrderRepository.findAdminOrder(orderId).orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        return AdminOrderDetailResponse.from(order);
    }

    @Transactional
    public void updateAdminOrderStatus(Long orderId, Order.Status status, String reason, User user){
        if(Order.Status.CANCELED.equals(status)){
            throw new IllegalArgumentException("주문취소는 불가능합니다.");
        }

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        Order.Status beforeStatus = order.getStatus();

        order.changeStatus(status);

        adminLogService.saveLog(user, TargetType.ORDER, orderId, AdminAction.UPDATE_ORDER_STATUS, reason, beforeStatus.name(),status.name());
    }


    @Transactional
    public void cancelOrder(Long orderId, String reason, User admin) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        if (order.getStatus() == Order.Status.CANCELED) {
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }

        if (order.getStatus() == Order.Status.COMPLETED) {
            throw new IllegalStateException("완료된 주문은 취소할 수 없습니다.");
        }

        Order.Status beforeStatus = order.getStatus();

        order.changeStatus(Order.Status.CANCELED);

        adminLogService.saveLog(admin, TargetType.ORDER, orderId, AdminAction.CANCEL_ORDER, reason, beforeStatus.name(), Order.Status.CANCELED.name());
    }

    @Transactional
    public void deleteOrder(Long orderId, String reason, User admin) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        if (order.isDeleted()) {
            throw new IllegalStateException("이미 삭제된 주문입니다.");
        }

        order.delete(true);

        adminLogService.saveLog(admin, TargetType.ORDER, orderId, AdminAction.DELETE_ORDER, reason, "false", "true");
    }
}
