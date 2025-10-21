package com.example.deliverytracker.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStore is a Querydsl query type for Store
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStore extends EntityPathBase<Store> {

    private static final long serialVersionUID = -690612770L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStore store = new QStore("store");

    public final BooleanPath active = createBoolean("active");

    public final StringPath address = createString("address");

    public final NumberPath<Double> averageRating = createNumber("averageRating", Double.class);

    public final QCategory category;

    public final EnumPath<DeliveryTime> currentDeliveryTime = createEnum("currentDeliveryTime", DeliveryTime.class);

    public final NumberPath<Integer> currentPrepTime = createNumber("currentPrepTime", Integer.class);

    public final NumberPath<Integer> deliveryFee = createNumber("deliveryFee", Integer.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<java.math.BigDecimal> minOrderAmount = createNumber("minOrderAmount", java.math.BigDecimal.class);

    public final StringPath name = createString("name");

    public final StringPath operatingHours = createString("operatingHours");

    public final com.example.deliverytracker.user.entitiy.QUser owner;

    public final StringPath phone = createString("phone");

    public final ListPath<Product, QProduct> products = this.<Product, QProduct>createList("products", Product.class, QProduct.class, PathInits.DIRECT2);

    public final NumberPath<Integer> reviewCount = createNumber("reviewCount", Integer.class);

    public final ListPath<com.example.deliverytracker.review.entity.Review, com.example.deliverytracker.review.entity.QReview> reviews = this.<com.example.deliverytracker.review.entity.Review, com.example.deliverytracker.review.entity.QReview>createList("reviews", com.example.deliverytracker.review.entity.Review.class, com.example.deliverytracker.review.entity.QReview.class, PathInits.DIRECT2);

    public QStore(String variable) {
        this(Store.class, forVariable(variable), INITS);
    }

    public QStore(Path<? extends Store> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStore(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStore(PathMetadata metadata, PathInits inits) {
        this(Store.class, metadata, inits);
    }

    public QStore(Class<? extends Store> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QCategory(forProperty("category")) : null;
        this.owner = inits.isInitialized("owner") ? new com.example.deliverytracker.user.entitiy.QUser(forProperty("owner")) : null;
    }

}

