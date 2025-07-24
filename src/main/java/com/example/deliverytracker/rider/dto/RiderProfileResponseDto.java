package com.example.deliverytracker.rider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RiderProfileResponseDto {
    private Long id;
    private String nickname;
    private String phone;
    private String email;
    private String status;
    private Integer totalDeliveries;
    private Double rating;
    private String region;
    private String profileImageUrl;
    private LocalDateTime createdAt;
}
