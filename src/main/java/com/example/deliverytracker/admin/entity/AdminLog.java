package com.example.deliverytracker.admin.entity;

import com.example.deliverytracker.common.BaseEntity;
import com.example.deliverytracker.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class AdminLog extends BaseEntity{

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User admin;

    private Long targetId;

    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    @Enumerated(EnumType.STRING)
    private AdminAction action;

    private String description;

    private LocalDateTime createdAt;

    private String beforeValue;

    private String afterValue;


    public static AdminLog of(User admin, TargetType targetType, Long targetId, AdminAction action, String description, String beforeValue, String afterValue){
        AdminLog log = new AdminLog();

        log.admin = admin;
        log.targetType = targetType;
        log.targetId = targetId;
        log.action = action;
        log.description = description;
        log.beforeValue = beforeValue;
        log.afterValue = afterValue;

        return log;
    }
}
