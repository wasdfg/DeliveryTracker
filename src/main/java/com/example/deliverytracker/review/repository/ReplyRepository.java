package com.example.deliverytracker.review.repository;

import com.example.deliverytracker.review.entity.ReviewReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<ReviewReply, Long> {
    boolean existsByReviewId(Long reviewId);
}
