package com.example.deliverytracker.admin.dto;

import com.example.deliverytracker.user.entity.User;
import lombok.Getter;

@Getter
public class UserStatusRequest {
    private User.Status status;
}
