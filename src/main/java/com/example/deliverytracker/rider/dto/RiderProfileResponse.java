package com.example.deliverytracker.rider.dto;

import com.example.deliverytracker.rider.entity.Rider;
import com.example.deliverytracker.user.entitiy.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RiderProfileResponse {
    private Long id;
    private String nickname;
    private String phone;
    private String email;
    private Rider.Status status;
    private String profileImageUrl;
    private LocalDateTime createdAt;

    public static RiderProfileResponse from(Rider rider){

        User user = rider.getUser();

        return RiderProfileResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .email(user.getEmail())
                .status(rider.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
