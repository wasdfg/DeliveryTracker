package com.example.deliverytracker.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignupRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String idForLogin;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    private String phone;

    private String address;

    private String imageUrl;
}
