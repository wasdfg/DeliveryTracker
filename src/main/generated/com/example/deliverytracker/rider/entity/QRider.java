package com.example.deliverytracker.rider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRider is a Querydsl query type for Rider
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRider extends EntityPathBase<Rider> {

    private static final long serialVersionUID = -776030594L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRider rider = new QRider("rider");

    public final com.example.deliverytracker.common.QBaseEntity _super = new com.example.deliverytracker.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Double> currentLat = createNumber("currentLat", Double.class);

    public final NumberPath<Double> currentLng = createNumber("currentLng", Double.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<Rider.Status> status = createEnum("status", Rider.Status.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final com.example.deliverytracker.user.entitiy.QUser user;

    public QRider(String variable) {
        this(Rider.class, forVariable(variable), INITS);
    }

    public QRider(Path<? extends Rider> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRider(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRider(PathMetadata metadata, PathInits inits) {
        this(Rider.class, metadata, inits);
    }

    public QRider(Class<? extends Rider> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.deliverytracker.user.entitiy.QUser(forProperty("user")) : null;
    }

}

