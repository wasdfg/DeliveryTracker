package com.example.deliverytracker.admin.dto;

import com.example.deliverytracker.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminUserResponse {

    private Long id;

    private String email;
    private String nickname;
    private String phone;
    private String address;
    private String role;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime withdrawnAt;
    private LocalDateTime suspendedUntil;

    public AdminUserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.role = user.getRole().name();
        this.status = user.getStatus().name();
        this.createdAt = user.getCreatedAt();
        this.withdrawnAt = user.getWithdrawnAt();
        this.suspendedUntil = user.getSuspendedUntil();
    }
}