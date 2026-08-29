package com.example.deliverytracker.admin.repository;

import com.example.deliverytracker.admin.dto.AdminProductSearchCondition;
import com.example.deliverytracker.store.entity.Product;
import com.example.deliverytracker.store.entity.QProduct;
import com.example.deliverytracker.store.entity.QStore;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminProductRepositoryImpl implements AdminProductRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    private final QProduct product = QProduct.product;
    private final QStore store = QStore.store;

    @Override
    public Page<Product> searchProducts(AdminProductSearchCondition condition, Pageable pageable) {

        List<Product> content = queryFactory
                .selectFrom(product)
                .leftJoin(product.store, store)
                .fetchJoin()
                .where(
                        productName(condition.getProductName()),
                        storeNameContains(condition.getStoreName()),
                        activeEq(condition.getActive()),
                        deletedEq(condition.getDeleted())
                )
                .orderBy(product.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(product.count())
                .from(product)
                .leftJoin(product.store, store)
                .where(
                        productName(condition.getProductName()),
                        storeNameContains(condition.getStoreName()),
                        activeEq(condition.getActive()),
                        deletedEq(condition.getDeleted())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    private BooleanExpression productName(String productName) {
        return StringUtils.hasText(productName)
                ? product.name.containsIgnoreCase(productName)
                : null;
    }


    private BooleanExpression storeNameContains(String storeName) {
        return StringUtils.hasText(storeName)
                ? product.store.name.containsIgnoreCase(storeName)
                : null;
    }

    private BooleanExpression activeEq(Boolean active) {
        return active == null
                ? null
                : product.isAvailable.eq(active);
    }


    private BooleanExpression deletedEq(Boolean deleted) {
        return deleted == null
                ? null
                : product.isDelete.eq(deleted);
    }


    @Override
    public Optional<Product> findAdminProduct(Long productId) {

        Product result = queryFactory
                .selectFrom(product)
                .leftJoin(product.store, store)
                .fetchJoin()
                .where(product.id.eq(productId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
