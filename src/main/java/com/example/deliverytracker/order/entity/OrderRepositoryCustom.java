package com.example.deliverytracker.order.entity;

import com.example.deliverytracker.order.dto.DailySalesDto;
import com.example.deliverytracker.order.dto.DayOfWeekStatsDto;
import com.example.deliverytracker.order.dto.HourlyStatsDto;
import com.example.deliverytracker.order.dto.MenuStatsDto;

import java.util.List;

public interface OrderRepositoryCustom {
    List<DailySalesDto> findDailySales(Long storeId);
    List<MenuStatsDto> findTopMenus(Long storeId);
    List<HourlyStatsDto> findHourlyStats(Long storeId);
    List<DayOfWeekStatsDto> findDayOfWeekStats(Long storeId);
}
