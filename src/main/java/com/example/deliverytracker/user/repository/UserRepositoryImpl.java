package com.example.deliverytracker.user.repository;

import com.example.deliverytracker.admin.dto.UserSearchCondition;
import com.example.deliverytracker.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.deliverytracker.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<User> searchUsers(UserSearchCondition condition, Pageable pageable) {

        List<User> content = queryFactory.selectFrom(user).where(
                                keyword(condition),
                                roleEq(condition.getRole()),
                                statusEq(condition.getStatus())
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        Long total = queryFactory
                        .select(user.count())
                        .from(user)
                        .where(
                                keyword(condition),
                                roleEq(condition.getRole()),
                                statusEq(condition.getStatus())
                        )
                        .fetchOne();

        return new PageImpl<>(
                content,
                pageable,
                total == null ? 0 : total
        );
    }

    private BooleanExpression keyword(UserSearchCondition condition){

        String keyword = condition.getKeyword();

        if(keyword == null || keyword.isBlank()){
            return null;
        }

        return switch (condition.getType()) {

            case "email" ->
                    user.email.contains(keyword);

            case "nickname" ->
                    user.nickname.contains(keyword);

            case "id" ->
                    user.idForLogin.contains(keyword);

            default ->
                    user.email.contains(keyword)
                            .or(
                                    user.nickname.contains(keyword)
                            )
                            .or(
                                    user.idForLogin.contains(keyword)
                            );
        };
    }

    private BooleanExpression roleEq(User.Role role){
        return role == null
                ? null
                : user.role.eq(role);
    }

    private BooleanExpression statusEq(User.Status status){
        return status == null
                ? null
                : user.status.eq(status);
    }
}
