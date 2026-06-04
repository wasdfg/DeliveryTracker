package com.example.deliverytracker.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOption is a Querydsl query type for Option
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOption extends EntityPathBase<Option> {

    private static final long serialVersionUID = -52229352L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOption option = new QOption("option");

    public final com.example.deliverytracker.common.QBaseEntity _super = new com.example.deliverytracker.common.QBaseEntity(this);

    public final NumberPath<java.math.BigDecimal> additionalPrice = createNumber("additionalPrice", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final QOptionGroup optionGroup;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QOption(String variable) {
        this(Option.class, forVariable(variable), INITS);
    }

    public QOption(Path<? extends Option> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOption(PathMetadata metadata, PathInits inits) {
        this(Option.class, metadata, inits);
    }

    public QOption(Class<? extends Option> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.optionGroup = inits.isInitialized("optionGroup") ? new QOptionGroup(forProperty("optionGroup"), inits.get("optionGroup")) : null;
    }

}

