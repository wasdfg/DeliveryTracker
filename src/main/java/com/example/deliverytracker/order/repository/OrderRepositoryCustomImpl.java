package com.example.deliverytracker.order.repository;

import com.example.deliverytracker.order.dto.DailySalesDto;
import com.example.deliverytracker.order.dto.DayOfWeekStatsDto;
import com.example.deliverytracker.order.dto.HourlyStatsDto;
import com.example.deliverytracker.order.dto.MenuStatsDto;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.entity.OrderRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.deliverytracker.order.entity.QOrder.order;
import static com.example.deliverytracker.order.entity.QOrderItem.orderItem;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<DailySalesDto> findDailySales(Long storeId, LocalDateTime start, LocalDateTime end) {
        return queryFactory
                .select(Projections.constructor(DailySalesDto.class,
                        order.createdAt.stringValue().substring(0, 10),
                        order.totalPrice.sum()
                ))
                .from(order)
                .where(
                        order.store.id.eq(storeId),
                        order.status.eq(Order.Status.COMPLETED),
                        order.createdAt.between(start, end)
                )
                .groupBy(order.createdAt.stringValue().substring(0, 10))
                .orderBy(order.createdAt.asc())
                .fetch();
    }

    @Override
    public List<MenuStatsDto> findTopMenus(Long storeId, LocalDateTime start, LocalDateTime end) {
        return queryFactory
                .select(Projections.constructor(MenuStatsDto.class,
                        orderItem.product.name,
                        orderItem.quantity.sum()
                ))
                .from(orderItem)
                .join(orderItem.order, order)
                .where(
                        order.store.id.eq(storeId),
                        order.status.eq(Order.Status.COMPLETED),
                        order.createdAt.between(start, end)
                )
                .groupBy(orderItem.product.name)
                .orderBy(orderItem.quantity.sum().desc())
                .limit(5)
                .fetch();
    }

    @Override
    public List<HourlyStatsDto> findHourlyStats(Long storeId, LocalDateTime start, LocalDateTime end) {
        return queryFactory
                .select(Projections.constructor(HourlyStatsDto.class,
                        order.createdAt.hour(),
                        order.count()
                ))
                .from(order)
                .where(
                        order.store.id.eq(storeId),
                        order.status.eq(Order.Status.COMPLETED),
                        order.createdAt.between(start, end)
                )
                .groupBy(order.createdAt.hour())
                .orderBy(order.createdAt.hour().asc())
                .fetch();
    }

    @Override
    public List<DayOfWeekStatsDto> findDayOfWeekStats(Long storeId, LocalDateTime start, LocalDateTime end) {
        return queryFactory
                .select(Projections.constructor(DayOfWeekStatsDto.class,
                        order.createdAt.dayOfWeek().stringValue(),
                        order.totalPrice.sum()
                ))
                .from(order)
                .where(
                        order.store.id.eq(storeId),
                        order.status.eq(Order.Status.COMPLETED),
                        order.createdAt.between(start, end)
                )
                .groupBy(order.createdAt.dayOfWeek())
                .fetch();
    }
}
