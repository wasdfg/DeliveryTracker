package com.example.deliverytracker.cart.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCartOption is a Querydsl query type for CartOption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCartOption extends EntityPathBase<CartOption> {

    private static final long serialVersionUID = 51846381L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCartOption cartOption = new QCartOption("cartOption");

    public final com.example.deliverytracker.common.QBaseEntity _super = new com.example.deliverytracker.common.QBaseEntity(this);

    public final QCartItem cartItem;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> optionId = createNumber("optionId", Long.class);

    public final NumberPath<java.math.BigDecimal> price = createNumber("price", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCartOption(String variable) {
        this(CartOption.class, forVariable(variable), INITS);
    }

    public QCartOption(Path<? extends CartOption> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCartOption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCartOption(PathMetadata metadata, PathInits inits) {
        this(CartOption.class, metadata, inits);
    }

    public QCartOption(Class<? extends CartOption> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cartItem = inits.isInitialized("cartItem") ? new QCartItem(forProperty("cartItem"), inits.get("cartItem")) : null;
    }

}

