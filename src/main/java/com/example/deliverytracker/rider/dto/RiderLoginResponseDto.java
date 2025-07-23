package com.example.deliverytracker.rider.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiderLoginResponseDto {
    private Long riderId;
    private String nickname;
    private String accessToken;
}
