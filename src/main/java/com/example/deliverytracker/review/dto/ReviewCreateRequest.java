package com.example.deliverytracker.review.dto;

import lombok.Getter;

@Getter
public class ReviewCreateRequest {

    private String content;

    private int rating;

    private String imageUrl;
}
