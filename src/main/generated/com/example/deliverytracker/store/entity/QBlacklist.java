package com.example.deliverytracker.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBlacklist is a Querydsl query type for Blacklist
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlacklist extends EntityPathBase<Blacklist> {

    private static final long serialVersionUID = -1698791526L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBlacklist blacklist = new QBlacklist("blacklist");

    public final com.example.deliverytracker.common.QBaseEntity _super = new com.example.deliverytracker.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath reason = createString("reason");

    public final QStore store;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.deliverytracker.user.entitiy.QUser user;

    public QBlacklist(String variable) {
        this(Blacklist.class, forVariable(variable), INITS);
    }

    public QBlacklist(Path<? extends Blacklist> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBlacklist(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBlacklist(PathMetadata metadata, PathInits inits) {
        this(Blacklist.class, metadata, inits);
    }

    public QBlacklist(Class<? extends Blacklist> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new QStore(forProperty("store"), inits.get("store")) : null;
        this.user = inits.isInitialized("user") ? new com.example.deliverytracker.user.entitiy.QUser(forProperty("user")) : null;
    }

}

