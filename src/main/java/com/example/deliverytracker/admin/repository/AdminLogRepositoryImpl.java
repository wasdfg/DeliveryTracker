package com.example.deliverytracker.admin.repository;

import com.example.deliverytracker.admin.dto.AdminLogSearchCondition;
import com.example.deliverytracker.admin.entity.AdminLog;
import com.example.deliverytracker.admin.entity.TargetType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminLogRepositoryImpl implements AdminLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminLog> searchLogs(AdminLogSearchCondition condition, Pageable pageable) {

        List<AdminLog> content = queryFactory
                .selectFrom(adminLog)
                .leftJoin(adminLog.admin, user)
                .fetchJoin()
                .where(
                        adminKeyword(condition),
                        targetTypeEq(condition.getTargetType()),
                        actionEq(condition.getAction()),
                        targetIdEq(condition.getTargetId()),
                        createdAtGoe(condition.getStartDate()),
                        createdAtLoe(condition.getEndDate())
                )
                .orderBy(adminLog.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(adminLog.count())
                .from(adminLog)
                .leftJoin(adminLog.admin, user)
                .where(
                        adminKeyword(condition),
                        targetTypeEq(condition.getTargetType()),
                        actionEq(condition.getAction()),
                        targetIdEq(condition.getTargetId()),
                        createdAtGoe(condition.getStartDate()),
                        createdAtLoe(condition.getEndDate())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    private BooleanExpression adminKeyword(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return user.nickname.contains(keyword).or(user.idForLogin.contains(keyword));
    }

    private BooleanExpression targetTypeEq(TargetType targetType) {

        return targetType == null
                ? null
                : adminLog.targetType.eq(targetType);
    }

    private BooleanExpression actionEq(AdminAction action) {

        return action == null
                ? null
                : adminLog.action.eq(action);
    }

    private BooleanExpression targetIdEq(Long targetId) {

        return targetId == null
                ? null
                : adminLog.targetId.eq(targetId);
    }
}
