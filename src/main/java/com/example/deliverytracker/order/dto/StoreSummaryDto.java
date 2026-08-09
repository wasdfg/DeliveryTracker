package com.example.deliverytracker.order.dto;

public record StoreSummaryDto(Long totalOrderCount, Long totalSales, Long averageOrderPrice, Long canceledOrderCount, Double cancellationRate) {
}