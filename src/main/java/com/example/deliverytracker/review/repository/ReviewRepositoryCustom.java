package com.example.deliverytracker.review.repository;

import com.example.deliverytracker.review.entity.Review;
import com.example.deliverytracker.user.entitiy.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    Page<Review> getReviewList(Long storeId, Pageable pageable);
    Page<Review> findByUser(User user, Pageable pageable);
}
