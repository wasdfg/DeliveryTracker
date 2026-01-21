package com.example.deliverytracker.order.dto;

import java.util.List;

public record OwnerStatsResponseDto(List<DailySalesDto> dailySales, List<MenuStatsDto> topMenus, Long totalSales, Long totalOrderCount){
}
