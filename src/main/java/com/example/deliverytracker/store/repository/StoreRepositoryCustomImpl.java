package com.example.deliverytracker.store.repository;

import com.example.deliverytracker.store.dto.StoreSearchCondition;
import com.example.deliverytracker.store.entity.Store;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.deliverytracker.store.entity.QStore.store;
import static com.example.deliverytracker.store.entity.QCategory.category;

public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public StoreRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Store> searchStores(StoreSearchCondition condition, Pageable pageable) {

        List<Store> content = queryFactory
                .selectFrom(store)
                .leftJoin(store.category, category).fetchJoin() // N+1 문제 해결을 위한 fetchJoin
                .where(

                        storeNameContains(condition.getStoreName()),
                        categoryNameEq(condition.getCategoryName())
                )
                .offset(pageable.getOffset()) // 페이징
                .limit(pageable.getPageSize()) // 페이징
                .fetch();


        Long total = queryFactory
                .select(store.count())
                .from(store)
                .where(
                        storeNameContains(condition.getStoreName()),
                        categoryNameEq(condition.getCategoryName())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }


    private BooleanExpression storeNameContains(String storeName) {
        return storeName != null ? store.name.contains(storeName) : null;
    }


    private BooleanExpression categoryNameEq(String categoryName) {
        return categoryName != null ? store.category.name.eq(categoryName) : null;
    }
}
