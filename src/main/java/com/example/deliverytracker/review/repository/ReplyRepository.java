package com.example.deliverytracker.review.repository;

import com.example.deliverytracker.review.entity.ReviewReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<ReviewReply, Long> {
    boolean existsByReviewId(Long reviewId);

    Optional<ReviewReply> findByReviewId(Long reviewId);
}
