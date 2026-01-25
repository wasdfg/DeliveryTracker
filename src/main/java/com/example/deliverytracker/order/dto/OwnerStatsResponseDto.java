package com.example.deliverytracker.order.dto;

import java.util.List;

public record OwnerStatsResponseDto(
        List<DailySalesDto> dailySales,
        List<MenuStatsDto> topMenus,
        List<HourlyStatsDto> hourlyStats,
        List<DayOfWeekStatsDto> dayOfWeekStats,
        Long totalSales,
        Long totalOrderCount
) {}
