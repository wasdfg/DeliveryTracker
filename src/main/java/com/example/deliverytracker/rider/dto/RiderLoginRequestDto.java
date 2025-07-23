package com.example.deliverytracker.rider.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RiderLoginRequestDto {
    private String idForLogin;
    private String password;
}
