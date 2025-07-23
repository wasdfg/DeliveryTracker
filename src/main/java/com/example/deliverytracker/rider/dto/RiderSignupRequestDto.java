package com.example.deliverytracker.rider.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RiderSignupRequestDto {
    private String email;
    private String idForLogin;
    private String password;
    private String nickname;
    private String phone;
}
