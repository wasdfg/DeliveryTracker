package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.Blacklist;

import java.time.LocalDateTime;

public class BlacklistResponse {
    private Long userId;
    private String userNickname;
    private String reason;
    private LocalDateTime createdAt;

    public BlacklistResponse(Blacklist blacklist) {
        this.userId = blacklist.getUser().getId();
        this.userNickname = blacklist.getUser().getNickname(); // User 엔티티에 nickname이 있다고 가정
        this.reason = blacklist.getReason() == null ? "사유 없음" : blacklist.getReason();
        this.createdAt = blacklist.getCreatedAt();
    }

}
