package com.example.deliverytracker.review.dto;

import com.example.deliverytracker.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

        List<String> productWithOptions = review.getOrder().getOrderItems().stream()
                .map(orderItem -> {
                    String productName = orderItem.getProduct().getName();

                    if (orderItem.getOrderOptions() != null && !orderItem.getOrderOptions().isEmpty()) {
                        String options = orderItem.getOrderOptions().stream()
                                .map(orderOption -> orderOption.getName())
                                .collect(Collectors.joining(", "));
                        return productName + " (" + options + ")";
                    }

                    return productName;
                })
                .toList();

        ReplyResponse reply = (review.getReviewReply() != null)
                ? new ReplyResponse(review.getReviewReply())
                : null;

        return new ReviewResponse(
                review.getUser().getNickname(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt(),
                productWithOptions,
                review.getImageUrl(),
                reply
        );
    }
}
