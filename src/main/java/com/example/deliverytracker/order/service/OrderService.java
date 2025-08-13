package com.example.deliverytracker.order.service;

import com.example.deliverytracker.order.dto.OrderCreateRequest;
import com.example.deliverytracker.order.dto.OrderResponse;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.entity.OrderItem;
import com.example.deliverytracker.order.repository.OrderRepository;
import com.example.deliverytracker.store.entity.Product;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.store.repository.ProductRepository;
import com.example.deliverytracker.store.repository.StoreRepository;
import com.example.deliverytracker.user.entitiy.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final StoreRepository storeRepository;

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    @Transactional
    public void createOrder(OrderCreateRequest request, User user){
        if(!user.getRole().equals(User.Role.USER)){
            throw new EntityNotFoundException("일반 사용자만 주문할 수 있습니다.");
        }

        Store store = storeRepository.findByIdAndActiveTrue(request.getStoreId())
                .orElseThrow(() -> new EntityNotFoundException("가게 정보가 없습니다."));

        List<OrderItem> orderItems = new ArrayList<>();

        int totalPrice = 0;

        for (OrderCreateRequest.Item item : request.getItems()) {
            Product product = productRepository.findByIdAndStoreId(item.getProductId(), store.getId())
                    .orElseThrow(() -> new EntityNotFoundException("상품 정보를 찾을 수 없습니다."));

            int itemTotal = product.getPrice() * item.getQuantity();
            totalPrice += itemTotal;

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .name(product.getName())
                    .price(product.getPrice())
                    .quantity(item.getQuantity())
                    .imageUrl(product.getImageUrl())
                    .build();

            orderItems.add(orderItem);
        }


        Order order = Order.builder()
                .user(user)
                .store(store)
                .deliveryAddress(request.getDeliveryAddress())
                .pickupAddress(request.getPickupAddress())
                .requestedAt(LocalDateTime.now())
                .status(Order.Status.REQUESTED)
                .totalPrice(totalPrice)
                .orderItems(orderItems)
                .build();



        for (OrderItem item : orderItems) {
            item.assignOrder(order);
        }

        orderRepository.save(order);

        redisPublisher.publish("order-channel", new OrderCreatedEvent(order.getId(), user.getId()));
    }

    @Transactional(readOnly = true)
    public OrderResponse findOrderInfo(Long id, User user){

        Order order = orderRepository.findOrderWithDetailsById(id)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        if (!order.getUser().getId().equals(user.getId())) {

            throw new EntityNotFoundException("주문을 조회할 권한이 없습니다.");
        }

        return OrderResponse.from(order);

    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> findAllOrders(String statusStr, Pageable pageable, User user){

        Page<Order> orderPage;

        if(statusStr != null && !statusStr.isBlank()){
            try{

                Order.Status status = Order.Status.valueOf(statusStr.toUpperCase());
                orderPage = orderRepository.findByUserAndStatus(user, status, pageable);
            } catch (IllegalArgumentException e) {

                throw new IllegalArgumentException("유효하지 않은 주문 상태입니다: " + statusStr);
            }
        }
        else{

            orderPage = orderRepository.findByUser(user, pageable);
        }

        return orderPage.map(OrderResponse::from);

    }

    @Transactional
    public void updateOrderStatus(Long orderId, String statusStr, User user){

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        if (!order.getStore().getOwner().getId().equals(user.getId())) {
            throw new EntityNotFoundException("주문 상태를 변경할 권한이 없습니다.");
        }

        Order.Status newStatus;

        try {
            newStatus = Order.Status.valueOf(statusStr.toUpperCase());
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 주문 상태입니다: " + statusStr);
        }

        order.changeStatus(newStatus);
    }

    @Transactional
    public void cancelOrder(Long orderId, User user){

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("주문 상태를 변경할 권한이 없습니다.");
        }

        Order.Status currentStatus = order.getStatus();
        if (currentStatus == Order.Status.COMPLETED || currentStatus == Order.Status.CANCELED) {
            throw new IllegalStateException("이미 완료되었거나 취소된 주문은 취소할 수 없습니다.");
        }

        order.cancel(Order.Status.CANCELED);
    }

    @Transactional
    public void deleteOrder(Long orderId, User user){

        if (user.getRole() != User.Role.ADMIN) {
            throw new AccessDeniedException("주문을 삭제할 권한이 없습니다.");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        order.delete(true);

    }
}
