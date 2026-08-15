package com.example.deliverytracker.admin.service;

import com.example.deliverytracker.admin.dto.AdminStatsResponse;
import com.example.deliverytracker.order.repository.OrderRepository;
import com.example.deliverytracker.store.repository.StoreRepository;
import com.example.deliverytracker.user.entity.User;
import com.example.deliverytracker.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminStatsService {

    private final UserRepository userRepository;

    private final StoreRepository storeRepository;

    private final OrderRepository orderRepository;

    public AdminStatsResponse getStats() {

        long totalUsers = userRepository.countByRole(User.Role.USER);

        long totalStores = storeRepository.count();

        long totalRiders = userRepository.countByRole(User.Role.RIDER);

        long withdrawnUsers = userRepository.countByStatus(User.Status.WITHDRAWN);

        LocalDate today = LocalDate.now();

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);

        long todayOrders = orderRepository.countTodayOrders(start, end);

        long todaySales = Optional.ofNullable(orderRepository.sumTodaySales(start, end)).orElse(0L);

        double todayAverageOrderPrice =
                todayOrders == 0 ? 0.0 : (double) todaySales / todayOrders;

        return new AdminStatsResponse(totalUsers, totalStores, totalRiders, withdrawnUsers, todayOrders, todaySales, todayAverageOrderPrice);
    }
}
