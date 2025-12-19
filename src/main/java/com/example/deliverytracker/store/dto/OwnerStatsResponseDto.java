package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.order.dto.DailySalesDto;
import com.example.deliverytracker.order.dto.MenuStatsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OwnerStatsResponseDto {
    private List<DailySalesDto> dailySales; // 일별 매출 리스트
    private List<MenuStatsDto> topMenus;    // 인기 메뉴 리스트
}
