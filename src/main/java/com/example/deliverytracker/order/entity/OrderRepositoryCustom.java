package com.example.deliverytracker.order.entity;

import com.example.deliverytracker.order.dto.DailySalesDto;
import com.example.deliverytracker.order.dto.MenuStatsDto;

import java.util.List;

public interface OrderRepositoryCustom {
    List<DailySalesDto> findDailySales(Long storeId);
    List<MenuStatsDto> findTopMenus(Long storeId);
}
