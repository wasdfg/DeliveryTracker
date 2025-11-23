package com.example.deliverytracker.store.repository;

import com.example.deliverytracker.store.dto.StoreSearchCondition;
import com.example.deliverytracker.store.entity.Store;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

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
                // ⚠️ 주의: store.category가 Entity(테이블) 관계일 때만 join 사용
                // store.category가 단순 Enum이라면 .leftJoin... 부분 삭제 필요
                .leftJoin(store.category, category).fetchJoin()
                .where(
                        // 👇 DTO의 'keyword' 필드를 가게 이름 검색에 사용
                        storeNameContains(condition.getKeyword()),
                        // 👇 DTO의 'categoryName' (또는 category) 필드 사용
                        categoryEq(condition.getCategory())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(store.count())
                .from(store)
                .where(
                        storeNameContains(condition.getKeyword()),
                        categoryEq(condition.getCategory())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
    
    private BooleanExpression storeNameContains(String keyword) {
        return StringUtils.hasText(keyword) ? store.name.contains(keyword) : null;
    }

    private BooleanExpression categoryEq(String categoryName) {
        if (!StringUtils.hasText(categoryName) || categoryName.equals("전체")) {
            return null;
        }

        return store.category.name.eq(categoryName);

    }
}
