package com.example.deliverytracker.review.dto;

import com.example.deliverytracker.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReviewResponse {

    private String userNickname;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    private List<String> orderedProductNames;

    public ReviewResponse(String userNickname, Integer rating, String content, LocalDateTime createdAt,List<String> orderedProductNames){
        this.userNickname = userNickname;
        this.rating = rating;
        this.content = content;
        this.createdAt = createdAt;
        this.orderedProductNames = orderedProductNames;
    }

    public static ReviewResponse from(Review review) {

        List<String> productNames = review.getOrder().getOrderItems().stream()
                .map(orderItem -> orderItem.getProduct().getName())
                .toList();

        return new ReviewResponse(
                review.getUser().getNickname(), // User 엔티티에 getNickname()이 있다고 가정
                review.getRating(),
                review.getContent(),
                review.getCreatedAt(),
                productNames
        );
    }
}
