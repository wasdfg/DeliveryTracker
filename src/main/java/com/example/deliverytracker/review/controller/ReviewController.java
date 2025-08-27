package com.example.deliverytracker.review.controller;

import com.example.deliverytracker.review.dto.ReviewResponse;
import com.example.deliverytracker.review.entity.Review;
import com.example.deliverytracker.review.service.ReviewService;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<String> writeReview(@PathVariable Long orderId, @RequestBody ReviewCreateRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails){
        this.reviewService.writeReview(orderId,request,userDetails.getUser());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("리뷰 등록이 완료되었습니다.");
    }

    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<Page<ReviewResponse>> getReviewList(@PathVariable Long storeId, Pageable pageable){
        Page<ReviewResponse> page = this.reviewService.getReviewList(storeId,pageable);

        return ResponseEntity.ok(page);
    }
}
