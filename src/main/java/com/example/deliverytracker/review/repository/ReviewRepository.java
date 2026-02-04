package com.example.deliverytracker.review.repository;

import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.review.entity.Review;
import com.example.deliverytracker.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    boolean existsByOrder(Order order);
    Optional<Review> findByOrder(Order order);
    List<Review> findAllByStore(Store store);
}
