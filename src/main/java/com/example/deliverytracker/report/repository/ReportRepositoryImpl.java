package com.example.deliverytracker.report.repository;

import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.report.dto.ReportSearchCondition;
import com.example.deliverytracker.report.entity.QReport;
import com.example.deliverytracker.report.entity.Report;
import com.example.deliverytracker.report.entity.ReportStatus;
import com.example.deliverytracker.report.entity.ReportType;
import com.example.deliverytracker.report.entity.TargetType;
import com.example.deliverytracker.user.entity.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QReport report = QReport.report;

    private final QUser user = QUser.user;

    @Override
    public Page<Report> searchReports(ReportSearchCondition condition, Pageable pageable) {
        List<Report> content = queryFactory
                .selectFrom(report)
                .leftJoin(report.reporter, user)
                .fetchJoin()
                .where(
                        reporterKeyword(condition.getReporterKeyword()),
                        reportTypeEq(condition.getReportType()),
                        statusEq(condition.getStatus()),
                        targetIdEq(condition.getTargetId()),
                        targetTypeEq(condition.getTargetType()),
                        createdDateGoe(condition.getStartDate()),
                        createdDateLoe(condition.getEndDate())
                )
                .orderBy(report.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(report.count())
                .from(report)
                .leftJoin(report.reporter, user)
                .where(
                        reporterKeyword(condition.getReporterKeyword()),
                        reportTypeEq(condition.getReportType()),
                        statusEq(condition.getStatus()),
                        targetIdEq(condition.getTargetId()),
                        targetTypeEq(condition.getTargetType()),
                        createdDateGoe(condition.getStartDate()),
                        createdDateLoe(condition.getEndDate())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    private BooleanExpression reporterKeyword(String keyword) {

        if (!StringUtils.hasText(keyword)) {
            return null;
        }

        return user.nickname.containsIgnoreCase(keyword).or(user.idForLogin.containsIgnoreCase(keyword));
    }

    private BooleanExpression reportTypeEq(ReportType reportType) {

        return reportType == null ? null : report.type.eq(reportType);
    }

    private BooleanExpression statusEq(ReportStatus status) {

        return status == null ? null : report.status.eq(status);
    }


    private BooleanExpression targetIdEq(Long targetId) {

        return targetId == null ? null : report.targetId.eq(targetId);
    }

    private BooleanExpression targetTypeEq(TargetType targetType) {

        return targetType == null ? null : report.targetType.eq(targetType);
    }

    private BooleanExpression createdDateGoe(LocalDate date) {

        return date == null ? null : report.createdAt.goe(date.atStartOfDay());
    }

    private BooleanExpression createdDateLoe(LocalDate date) {

        return date == null ? null : report.createdAt.lt(date.plusDays(1).atStartOfDay());
    }

    @Override
    public Optional<Report> findReport(Long reportId) {

        Report result = queryFactory
                .selectFrom(report)
                .leftJoin(report.reporter, user)
                .fetchJoin()
                .where(report.id.eq(reportId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
