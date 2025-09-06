package com.example.deliverytracker.review.repository;

import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.review.entity.Review;
import com.example.deliverytracker.user.entitiy.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    boolean existsByOrder(Order order);

    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.user u " +
            "JOIN FETCH r.order o " +
            "WHERE r.store.id = :storeId")
    Page<Review> getReviewList(Long storeId, Pageable pageable);

    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.order o " +
            "JOIN FETCH o.store s " +
            "WHERE r.user = :user")
    Page<Review> findByUser(User user, Pageable pageable);
}
