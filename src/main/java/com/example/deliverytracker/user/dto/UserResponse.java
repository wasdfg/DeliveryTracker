package com.example.deliverytracker.user.dto;

import com.example.deliverytracker.user.entity.User;

import java.time.LocalDateTime;

public class UserResponse {
    private String email;
    private String nickname;
    private String phone;
    private String address;
    private String role;

    private String status;

    private LocalDateTime createdAt;

    public UserResponse(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.phone = user.getPhone();
        this.role = user.getRole().name();
        this.address = user.getAddress();
        this.status = user.getStatus().name();
        this.createdAt = user.getCreatedAt();
    }
}
