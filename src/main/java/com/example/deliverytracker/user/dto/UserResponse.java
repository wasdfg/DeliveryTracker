package com.example.deliverytracker.user.dto;

import com.example.deliverytracker.user.entitiy.User;

public class UserResponse {
    private String email;
    private String nickname;
    private String phone;
    private String address;
    private String role;

    public UserResponse(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.phone = user.getPhone();
        this.role = user.getRole().name();
        this.address = user.getAddress();
    }
}
