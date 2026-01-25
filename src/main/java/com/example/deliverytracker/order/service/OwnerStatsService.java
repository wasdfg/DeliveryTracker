package com.example.deliverytracker.order.service;

import com.example.deliverytracker.order.dto.DailySalesDto;
import com.example.deliverytracker.order.dto.DayOfWeekStatsDto;
import com.example.deliverytracker.order.dto.HourlyStatsDto;
import com.example.deliverytracker.order.dto.MenuStatsDto;
import com.example.deliverytracker.order.repository.OrderRepository;
import com.example.deliverytracker.order.dto.OwnerStatsResponseDto;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.store.repository.StoreRepository;
import com.example.deliverytracker.user.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerStatsService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    public OwnerStatsResponseDto getStats(Long storeId, User user) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인 소유의 가게 통계만 조회할 수 있습니다.");
        }

        List<DailySalesDto> dailySales = orderRepository.findDailySales(storeId);
        List<MenuStatsDto> topMenus = orderRepository.findTopMenus(storeId);
        List<HourlyStatsDto> hourlyStats = orderRepository.findHourlyStats(storeId);
        List<DayOfWeekStatsDto> dayOfWeekStats = orderRepository.findDayOfWeekStats(storeId);

        long totalSales = dailySales.stream().mapToLong(DailySalesDto::totalSales).sum();
        long totalOrderCount = hourlyStats.stream().mapToLong(HourlyStatsDto::orderCount).sum();

        return new OwnerStatsResponseDto(dailySales, topMenus, hourlyStats, dayOfWeekStats, totalSales, totalOrderCount);
    }
}