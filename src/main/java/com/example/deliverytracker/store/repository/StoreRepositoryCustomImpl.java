package com.example.deliverytracker.store.repository;

import com.example.deliverytracker.admin.dto.AdminStoreSearchCondition;
import com.example.deliverytracker.store.dto.StoreSearchCondition;
import com.example.deliverytracker.store.entity.Store;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.deliverytracker.store.entity.QStore.store;
import static com.example.deliverytracker.store.entity.QCategory.category;
import static com.example.deliverytracker.user.entity.QUser.user;

public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public StoreRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Store> searchStores(StoreSearchCondition condition, Pageable pageable) {

        List<Store> content = queryFactory
                .selectFrom(store)
                .leftJoin(store.category, category).fetchJoin()
                .where(
                        keywordLike(condition.getKeyword()),
                        categoryIdEq(condition.getCategoryId()),
                        minOrderAmountLoe(condition.getMinOrderAmount()),
                        deliveryFeeLoe(condition.getDeliveryFee())
                )
                .orderBy(store.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(store.count())
                .from(store)
                .where(
                        keywordLike(condition.getKeyword()),
                        categoryIdEq(condition.getCategoryId()),
                        minOrderAmountLoe(condition.getMinOrderAmount()),
                        deliveryFeeLoe(condition.getDeliveryFee())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
    
    private BooleanExpression keywordLike(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }

        return store.name.contains(keyword);
    }

    private BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId != null ? store.category.id.eq(categoryId) : null;
    }

    private BooleanExpression minOrderAmountLoe(Integer minOrderAmount) {
        return minOrderAmount != null ? store.minOrderAmount.loe(minOrderAmount) : null;
    }

    private BooleanExpression deliveryFeeLoe(Integer deliveryFee) {
        return deliveryFee != null ? store.deliveryFee.loe(deliveryFee) : null;
    }

    @Override
    public Page<Store> searchStoresForAdmin(AdminStoreSearchCondition condition, Pageable pageable){

        List<Store> content = queryFactory
                        .selectFrom(store)
                        .leftJoin(store.category, category)
                        .fetchJoin()
                        .leftJoin(store.owner, user)
                        .fetchJoin()
                        .where(
                                keywordLike(condition.getKeyword()),
                                categoryIdEq(condition.getCategoryId()),
                                activeEq(condition.getActive()),
                                deletedEq(condition.getDeleted()),
                                ownerIdEq(condition.getOwnerId())
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(store.id.desc())
                        .fetch();

        return PageableExecutionUtils.getPage(content, pageable, () -> countAdmin(condition));
    }

    private BooleanExpression activeEq(Boolean active) {
        return active != null
                ? store.active.eq(active)
                : null;
    }

    private BooleanExpression deletedEq(Boolean deleted) {
        return deleted != null
                ? store.isDeleted.eq(deleted)
                : null;
    }

    private BooleanExpression ownerIdEq(Long ownerId) {
        return ownerId != null
                ? store.owner.id.eq(ownerId)
                : null;
    }

    private Long countAdmin(AdminStoreSearchCondition condition) {

        return queryFactory
                .select(store.count())
                .from(store)
                .where(
                        keywordLike(condition.getKeyword()),
                        categoryIdEq(condition.getCategoryId()),
                        activeEq(condition.getActive()),
                        deletedEq(condition.getDeleted()),
                        ownerIdEq(condition.getOwnerId())
                )
                .fetchOne();
    }
}
