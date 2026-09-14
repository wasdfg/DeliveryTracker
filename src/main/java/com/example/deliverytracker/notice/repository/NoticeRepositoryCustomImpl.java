package com.example.deliverytracker.notice.repository;

import com.example.deliverytracker.notice.dto.NoticeSearchCondition;
import com.example.deliverytracker.notice.entity.Notice;
import com.example.deliverytracker.notice.entity.QNotice;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QNotice notice = QNotice.notice;

    @Override
    public Page<Notice> searchNotices(
            NoticeSearchCondition condition,
            Pageable pageable
    ) {

        List<Notice> content = queryFactory
                .selectFrom(notice)
                .where(
                        keywordContains(condition.getKeyword()),
                        importantEq(condition.getImportant()),
                        visibleEq(condition.getVisible())
                )
                .orderBy(notice.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(notice.count())
                .from(notice)
                .where(
                        keywordContains(condition.getKeyword()),
                        importantEq(condition.getImportant()),
                        visibleEq(condition.getVisible())
                )
                .fetchOne();

        return new PageImpl<>(
                content,
                pageable,
                total == null ? 0 : total
        );
    }

    private BooleanExpression keywordContains(String keyword) {

        if (!StringUtils.hasText(keyword)) {
            return null;
        }

        return notice.title.containsIgnoreCase(keyword)
                .or(notice.content.containsIgnoreCase(keyword));
    }

    private BooleanExpression importantEq(Boolean important) {

        if (important == null) {
            return null;
        }

        return notice.important.eq(important);
    }

    private BooleanExpression visibleEq(Boolean visible) {

        if (visible == null) {
            return null;
        }

        return notice.visible.eq(visible);
    }
}