package com.example.deliverytracker.admin.dto;

import com.example.deliverytracker.admin.entity.AdminLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminLogResponse {
    private Long id;

    private String admin;

    private String targetType;

    private Long targetId;

    private String action;

    private String description;

    private String beforeValue;

    private String afterValue;

    private LocalDateTime createdAt;

    public static AdminLogResponse from(AdminLog log) {

        return new AdminLogResponse(log.getId(),
                log.getAdmin().getNickname(),
                log.getTargetType().name(),
                log.getTargetId(),
                log.getAction().name(),
                log.getDescription(),
                log.getBeforeValue(),
                log.getAfterValue(),
                log.getCreatedAt()
        );
    }
}
