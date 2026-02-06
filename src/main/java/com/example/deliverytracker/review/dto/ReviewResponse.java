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
    private String imageUrl;
    private LocalDateTime createdAt;
    private List<String> orderedProductNames;
    private ReplyResponse reply;

    public ReviewResponse(String userNickname, Integer rating, String content, LocalDateTime createdAt,List<String> orderedProductNames,String imageUrl, ReplyResponse reply){
        this.userNickname = userNickname;
        this.rating = rating;
        this.content = content;
        this.createdAt = createdAt;
        this.orderedProductNames = orderedProductNames;
        this.imageUrl = imageUrl;
        this.reply = reply;
    }

    public static ReviewResponse from(Review review) {

        List<String> productNames = review.getOrder().getOrderItems().stream()
                .map(orderItem -> orderItem.getProduct().getName())
                .toList();

        ReplyResponse reply = null;
        if (review.getReviewReply() != null) {
            reply = new ReplyResponse(
                    review.getReviewReply()
            );
        }

        return new ReviewResponse(
                review.getUser().getNickname(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt(),
                productNames,
                review.getImageUrl(),
                reply
        );
    }
}
