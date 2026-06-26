package com.example.deliverytracker.admin.dto;

import com.example.deliverytracker.user.entity.User;
import lombok.Getter;

@Getter
public class UserSearchCondition {

    private String type;

    private String keyword;

    private User.Role role;

    private User.Status status;
}