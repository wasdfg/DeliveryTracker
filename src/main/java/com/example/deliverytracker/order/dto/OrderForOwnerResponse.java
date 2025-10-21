package com.example.deliverytracker.order.dto;

import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.review.entity.Review;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderForOwnerResponse {
    private Long orderId;

    private LocalDateTime orderDate;

    private String deliveryAddress;

    private String customerPhoneNumber;

    private Order.Status status;

    private BigDecimal totalPrice;

    private List<OrderItemResponse> orderItems;

    private ReviewInfo reviewInfo;

    public OrderForOwnerResponse(Long orderId, LocalDateTime orderDate, String deliveryAddress, String customerPhoneNumber, Order.Status status, List<OrderItemResponse> orderItems, BigDecimal totalPrice, ReviewInfo reviewInfo) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.deliveryAddress = deliveryAddress;
        this.customerPhoneNumber = customerPhoneNumber;
        this.status = status;
        this.totalPrice = totalPrice;
        this.orderItems = orderItems;
        this.reviewInfo = reviewInfo;
    }

    public static OrderForOwnerResponse from(Order order) {

        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(OrderItemResponse::from)
                .toList();


        return new OrderForOwnerResponse(order.getId(), order.getRequestedAt(), order.getDeliveryAddress(), order.getUser().getAddress(), order.getStatus(),itemResponses, order.getTotalPrice(), order.getReview() != null ? ReviewInfo.from(order.getReview()) : null);
    }

    @Getter
    private static class ReviewInfo {
        private Long reviewId;
        private int rating;
        private String content;
        private String nickname; // 리뷰를 작성한 고객 닉네임

        public static ReviewInfo from(Review review) {
            return new ReviewInfo(
                    review.getId(),
                    review.getRating(),
                    review.getContent(),
                    review.getUser().getNickname()
            );
        }

        public ReviewInfo(Long reviewId,int rating,String content,String nickname){
            this.reviewId = reviewId;
            this.rating = rating;
            this.content = content;
            this.nickname = nickname;
        }
    }

}
