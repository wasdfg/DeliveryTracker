package com.example.deliverytracker.admin.dto;

public record AdminStatsResponse(Long totalUsers, Long totalStores, Long totalRiders, Long withdrawnUsers
                                    , Long todayOrders, Long todaySales, Double todayAverageOrderPrice) {
}
