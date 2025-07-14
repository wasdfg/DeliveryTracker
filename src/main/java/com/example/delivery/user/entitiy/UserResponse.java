package com.example.delivery.user.entitiy;

public class UserResponse {
    private String email;
    private String nickname;
    private String phone;
    private String role;

    public UserResponse(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.phone = user.getPhone();
        this.role = user.getRole().name();
    }
}
