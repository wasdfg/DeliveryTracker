package com.example.deliverytracker.admin.dto;

public record AdminDashboardResponse(Long totalUsers, Long totalStores, Long totalRiders, Long withdrawnUsers
                                    , Long todayOrders, Long todaySales, Double todayAverageOrderPrice) {
}
