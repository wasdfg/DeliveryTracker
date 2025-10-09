package com.example.deliverytracker.user.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmTokenRequestDto {

    @NotBlank(message = "FCM 토큰은 비어 있을 수 없습니다.")
    private String fcmToken;
}