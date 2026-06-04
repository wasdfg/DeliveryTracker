package com.example.deliverytracker.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOptionGroup is a Querydsl query type for OptionGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOptionGroup extends EntityPathBase<OptionGroup> {

    private static final long serialVersionUID = -1956763065L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOptionGroup optionGroup = new QOptionGroup("optionGroup");

    public final com.example.deliverytracker.common.QBaseEntity _super = new com.example.deliverytracker.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isRequired = createBoolean("isRequired");

    public final StringPath name = createString("name");

    public final ListPath<Option, QOption> options = this.<Option, QOption>createList("options", Option.class, QOption.class, PathInits.DIRECT2);

    public final QProduct product;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QOptionGroup(String variable) {
        this(OptionGroup.class, forVariable(variable), INITS);
    }

    public QOptionGroup(Path<? extends OptionGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOptionGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOptionGroup(PathMetadata metadata, PathInits inits) {
        this(OptionGroup.class, metadata, inits);
    }

    public QOptionGroup(Class<? extends OptionGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

