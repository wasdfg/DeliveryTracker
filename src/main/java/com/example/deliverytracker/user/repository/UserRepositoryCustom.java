package com.example.deliverytracker.user.repository;

import com.example.deliverytracker.admin.dto.UserSearchCondition;
import com.example.deliverytracker.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    Page<User> searchUsers(UserSearchCondition condition, Pageable pageable);

}