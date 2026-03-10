package com.example.deliverytracker.review.repository;

import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.review.entity.Review;
import com.example.deliverytracker.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    boolean existsByOrder(Order order);
    Optional<Review> findByOrder(Order order);
    List<Review> findAllByStore(Store store);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.store.id = :storeId AND r.createdAt BETWEEN :start AND :end")
    Double findAverageRatingByStoreId(Long storeId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT " +
            "COUNT(CASE WHEN r.reply IS NOT NULL THEN 1 END) * 100.0 / COUNT(r) " +
            "FROM Review r WHERE r.store.id = :storeId AND r.createdAt BETWEEN :start AND :end")
    Double calculateReplyRate(Long storeId, LocalDateTime start, LocalDateTime end);
}
