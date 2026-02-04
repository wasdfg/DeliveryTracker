package com.example.deliverytracker.review.repository;

import static com.example.deliverytracker.review.entity.QReview.review;
import static com.example.deliverytracker.review.entity.QReviewReply.reviewReply;
import static com.example.deliverytracker.order.entity.QOrder.order;
import static com.example.deliverytracker.store.entity.QStore.store;

import com.example.deliverytracker.review.entity.Review;
import com.example.deliverytracker.user.entitiy.QUser;
import com.example.deliverytracker.user.entitiy.User;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Review> getReviewList(Long storeId, Pageable pageable) {
        List<Review> content = queryFactory
                .selectFrom(review)
                .leftJoin(review.user, QUser.user).fetchJoin()
                .leftJoin(review.order, order).fetchJoin()
                .leftJoin(review.reviewReply, reviewReply).fetchJoin() // 답글 포함
                .where(review.store.id.eq(storeId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(review.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(review.count())
                .from(review)
                .where(review.store.id.eq(storeId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Review> findByUser(User userEntity, Pageable pageable) {
        List<Review> content = queryFactory
                .selectFrom(review)
                .leftJoin(review.order, order).fetchJoin()
                .leftJoin(order.store, store).fetchJoin()
                .leftJoin(review.reviewReply, reviewReply).fetchJoin()
                .where(review.user.eq(userEntity))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(review.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(review.count())
                .from(review)
                .where(review.user.eq(userEntity));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
