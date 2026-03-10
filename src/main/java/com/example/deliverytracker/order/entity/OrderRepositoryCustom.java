package com.example.deliverytracker.order.entity;

import com.example.deliverytracker.order.dto.DailySalesDto;
import com.example.deliverytracker.order.dto.DayOfWeekStatsDto;
import com.example.deliverytracker.order.dto.HourlyStatsDto;
import com.example.deliverytracker.order.dto.MenuStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepositoryCustom {
    List<DailySalesDto> findDailySales(Long storeId, LocalDateTime start, LocalDateTime end);
    List<MenuStatsDto> findTopMenus(Long storeId, LocalDateTime start, LocalDateTime end);
    List<HourlyStatsDto> findHourlyStats(Long storeId, LocalDateTime start, LocalDateTime end);
    List<DayOfWeekStatsDto> findDayOfWeekStats(Long storeId, LocalDateTime start, LocalDateTime end);

    long countOrders(Long storeId, LocalDateTime start, LocalDateTime end);

    long countCancelledOrders(Long storeId, LocalDateTime start, LocalDateTime end);

    double calculateRetentionRate(Long storeId, LocalDateTime start, LocalDateTime end);
}
