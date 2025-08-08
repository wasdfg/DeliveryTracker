package com.example.deliverytracker.order.service;

import com.example.deliverytracker.order.dto.OrderCreateRequest;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.entity.OrderItem;
import com.example.deliverytracker.order.repository.OrderRepository;
import com.example.deliverytracker.store.entity.Product;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.store.repository.StoreRepository;
import com.example.deliverytracker.user.entitiy.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final StoreRepository storeRepository;

    private final OrderRepository orderRepository;

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
}
