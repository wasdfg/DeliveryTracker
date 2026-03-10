package com.example.deliverytracker.order.dto;

import java.time.LocalDate;
import java.util.List;

public record OwnerStatsResponseDto(
        List<DailySalesDto> dailySales,
        List<MenuStatsDto> topMenus,
        List<HourlyStatsDto> hourlyStats,
        List<DayOfWeekStatsDto> dayOfWeekStats,
        Long totalSales,
        Long totalOrderCount,
        Long averageOrderValue,
        Double customerRetentionRate,
        Double cancellationRate,
        Double averageRating,
        Double replyRate,

        LocalDate startDate,
        LocalDate endDate
) {}
