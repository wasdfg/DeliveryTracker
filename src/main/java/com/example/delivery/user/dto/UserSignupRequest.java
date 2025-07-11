package com.example.delivery.user.dto;

import jakarta.validation.constraints.NotBlank;

public class UserSignupRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;
}
