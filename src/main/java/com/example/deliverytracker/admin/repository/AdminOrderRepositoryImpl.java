package com.example.deliverytracker.admin.repository;

import com.example.deliverytracker.admin.dto.AdminOrderSearchCondition;
import com.example.deliverytracker.order.entity.Order;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminOrderRepositoryImpl implements AdminOrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Order> searchOrders(AdminOrderSearchCondition condition, Pageable pageable) {

        List<Order> content = queryFactory
                .selectFrom(order)
                .leftJoin(order.user, user)
                .fetchJoin()
                .leftJoin(order.store, store)
                .fetchJoin()
                .where(
                        orderIdEq(condition.getOrderId()),
                        userKeyword(condition.getUserKeyword()),
                        storeKeyword(condition.getStoreKeyword()),
                        statusEq(condition.getStatus()),
                        createdDateGoe(condition.getStartDate()),
                        createdDateLoe(condition.getEndDate())
                )
                .orderBy(order.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(order.count())
                .from(order)
                .leftJoin(order.user, user)
                .leftJoin(order.store, store)
                .where(
                        orderIdEq(condition.getOrderId()),
                        userKeyword(condition.getUserKeyword()),
                        storeKeyword(condition.getStoreKeyword()),
                        statusEq(condition.getStatus()),
                        createdDateGoe(condition.getStartDate()),
                        createdDateLoe(condition.getEndDate())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    private BooleanExpression orderIdEq(Long orderId) {
        return orderId == null
                ? null
                : order.id.eq(orderId);
    }

    private BooleanExpression userKeyword(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return user.nickname.contains(keyword)
                .or(user.idForLogin.contains(keyword));
    }

    private BooleanExpression storeKeyword(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return store.name.contains(keyword);
    }

    private BooleanExpression statusEq(Order.Status status) {

        return status == null
                ? null
                : order.status.eq(status);
    }

    private BooleanExpression createdDateGoe(LocalDate date) {

        return date == null
                ? null
                : order.createdAt.goe(date.atStartOfDay());
    }

    private BooleanExpression createdDateLoe(LocalDate date) {

        return date == null
                ? null
                : order.createdAt.lt(date.plusDays(1).atStartOfDay());
    }

    @Override
    public Optional<Order> findAdminOrder(Long orderId) {

        Order result = queryFactory
                .selectFrom(order)
                .leftJoin(order.user, user)
                .fetchJoin()
                .leftJoin(order.store, store)
                .fetchJoin()
                .where(order.id.eq(orderId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
