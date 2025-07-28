package com.example.deliverytracker.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPasswordRequest {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String toChangePassword1;

    @NotBlank
    private String toChangePassword2;
}
