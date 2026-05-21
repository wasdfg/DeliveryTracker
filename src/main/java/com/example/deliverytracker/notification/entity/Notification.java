package com.example.deliverytracker.notification.entity;

import com.example.deliverytracker.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long receiverId;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String targetUrl;

    private boolean isRead = false;

    private Long referenceId;

    private Long senderId;

    @Builder
    public Notification(Long receiverId, String title, String content, NotificationType type, String targetUrl, Long referenceId, Long senderId) {
        this.receiverId = receiverId;
        this.title = title;
        this.content = content;
        this.type = type;
        this.targetUrl = targetUrl;
        this.referenceId = referenceId;
        this.senderId = senderId;
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public void markAsUnread() {
        this.isRead = false;
    }
}
