package com.example.deliverytracker.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOperationTime is a Querydsl query type for OperationTime
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOperationTime extends EntityPathBase<OperationTime> {

    private static final long serialVersionUID = 1586258129L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOperationTime operationTime = new QOperationTime("operationTime");

    public final TimePath<java.time.LocalTime> closeTime = createTime("closeTime", java.time.LocalTime.class);

    public final EnumPath<java.time.DayOfWeek> dayOfWeek = createEnum("dayOfWeek", java.time.DayOfWeek.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDayOff = createBoolean("isDayOff");

    public final TimePath<java.time.LocalTime> openTime = createTime("openTime", java.time.LocalTime.class);

    public final QStore store;

    public QOperationTime(String variable) {
        this(OperationTime.class, forVariable(variable), INITS);
    }

    public QOperationTime(Path<? extends OperationTime> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOperationTime(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOperationTime(PathMetadata metadata, PathInits inits) {
        this(OperationTime.class, metadata, inits);
    }

    public QOperationTime(Class<? extends OperationTime> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new QStore(forProperty("store"), inits.get("store")) : null;
    }

}

