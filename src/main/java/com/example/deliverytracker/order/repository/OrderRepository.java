package com.example.deliverytracker.order.repository;

import com.example.deliverytracker.order.dto.DailySalesDto;
import com.example.deliverytracker.order.dto.MenuStatsDto;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.entity.OrderRepositoryCustom;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.user.entitiy.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long>, OrderRepositoryCustom {

    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.store s " +
            "JOIN FETCH o.orderItems oi " +
            "JOIN FETCH oi.product p " +
            "WHERE o.id = :orderId")
    Optional<Order> findOrderWithDetailsById(@Param("orderId") Long orderId);

    Page<Order> findByUserAndStatus(User user, Order.Status status, Pageable pageable);

    Page<Order> findByUser(User user, Pageable pageable);

    Page<Order> findByStore(Store store, Pageable pageable);

    Page<Order> findAllByUserOrderByRequestedAtDesc(User user, Pageable pageable);

    Page<Order> findAllByUserAndStatusOrderByRequestedAtDesc(User user, Order.Status status, Pageable pageable);

    @Query(value = "SELECT DATE(created_at) as date, SUM(total_price) as totalSales " +
            "FROM orders " +
            "WHERE store_id = :storeId " +
            "AND status != 'CANCELED' " +
            "AND created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY) " +
            "GROUP BY DATE(created_at) " +
            "ORDER BY date ASC", nativeQuery = true)
    List<DailySalesDto> findDailySales(@Param("storeId") Long storeId);


    @Query("SELECT m.name as menuName, SUM(oi.quantity) as count " +
            "FROM OrderItem oi " +
            "JOIN oi.menu m " +
            "JOIN oi.order o " +
            "WHERE o.store.id = :storeId " +
            "AND o.status != 'CANCELED' " +
            "GROUP BY m.name " +
            "ORDER BY count DESC " +
            "LIMIT 5")
    List<MenuStatsDto> findTopMenus(@Param("storeId") Long storeId);
}
