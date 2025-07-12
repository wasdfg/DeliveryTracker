package com.example.delivery.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequest {
    @NotBlank
    private String idForLogin;

    @NotBlank
    private String password;
}
