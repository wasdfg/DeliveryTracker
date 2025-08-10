package com.example.deliverytracker.order.dto;

import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.store.dto.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderResponse {

    private Long id;

    private Order.Status status;

    private int totalPrice;

    private StoreResponse store;

    private List<OrderItemResponse> orderItems;

    private OrderResponse(Long id, Order.Status status, int totalPrice, StoreResponse store, List<OrderItemResponse> orderItems) {
        this.id = id;
        this.status = status;
        this.totalPrice = totalPrice;
        this.store = store;
        this.orderItems = orderItems;
    }

    public static OrderResponse from(Order order) {

        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(OrderItemResponse::from)
                .toList();

        // StoreResponse도 Store 엔티티를 받아 생성하도록 static from 메서드를 만드는 것을 추천합니다.
        StoreResponse storeResponse = StoreResponse.from(order.getStore());

        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getTotalPrice(), // 주문 생성 시 계산된 총 금액을 Order 엔티티에 저장하고 그 값을 사용
                storeResponse,
                itemResponses
        );
    }
}
