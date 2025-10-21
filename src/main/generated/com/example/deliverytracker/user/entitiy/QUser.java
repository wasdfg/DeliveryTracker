package com.example.deliverytracker.user.entitiy;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -812375877L;

    public static final QUser user = new QUser("user");

    public final com.example.deliverytracker.common.QBaseEntity _super = new com.example.deliverytracker.common.QBaseEntity(this);

    public final StringPath address = createString("address");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final StringPath fcmToken = createString("fcmToken");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath idForLogin = createString("idForLogin");

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final ListPath<com.example.deliverytracker.review.entity.Review, com.example.deliverytracker.review.entity.QReview> reviews = this.<com.example.deliverytracker.review.entity.Review, com.example.deliverytracker.review.entity.QReview>createList("reviews", com.example.deliverytracker.review.entity.Review.class, com.example.deliverytracker.review.entity.QReview.class, PathInits.DIRECT2);

    public final EnumPath<User.Role> role = createEnum("role", User.Role.class);

    public final EnumPath<User.Status> status = createEnum("status", User.Status.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

