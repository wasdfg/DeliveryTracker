package com.example.delivery.user.dto;

import jakarta.validation.constraints.NotBlank;

public class UserLoginRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
