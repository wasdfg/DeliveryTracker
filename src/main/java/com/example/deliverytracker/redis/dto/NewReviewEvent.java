package com.example.deliverytracker.redis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class NewReviewEvent implements Serializable {

    private Long reviewId;
    private Long storeId;      // 어느 가게에 대한 리뷰인지
    private String authorName; // 리뷰 작성자 이름
    private double rating;     // 남긴 별점
    private String content;    // 리뷰 내용
    private String message;

    public NewReviewEvent(Long reviewId, Long storeId, String authorName, double rating, String content) {
        this.reviewId = reviewId;
        this.storeId = storeId;
        this.authorName = authorName;
        this.rating = rating;
        this.content = content;
        this.message = authorName + "님이 새 리뷰를 남겼습니다: \"" + content + "\"";
    }
}