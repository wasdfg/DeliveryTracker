package com.example.deliverytracker.review.dto;

import lombok.Getter;

@Getter
public class ReviewUpdateRequest {
    private String content;

    private Integer rating;

    private String imageUrl;

    private Boolean deleteImage;
}
