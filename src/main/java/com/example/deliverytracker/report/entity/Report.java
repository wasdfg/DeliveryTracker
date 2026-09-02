package com.example.deliverytracker.report.entity;

import com.example.deliverytracker.common.BaseEntity;
import com.example.deliverytracker.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetType targetType;

    @Column(nullable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;
    
    @Column(columnDefinition = "TEXT")
    private String adminComment;

    private LocalDateTime resolvedAt;


    public Report(User reporter, TargetType targetType, Long targetId, ReportType type, String description) {
        this.reporter = reporter;
        this.targetType = targetType;
        this.targetId = targetId;
        this.type = type;
        this.description = description;
        this.status = ReportStatus.WAITING;
    }

    
    public void startProcess() { //반드시 서비스단에서 체크
        this.status = ReportStatus.IN_PROGRESS;
    }
    
    public void resolve(String adminComment) {
        this.status = ReportStatus.RESOLVED;
        this.adminComment = adminComment;
        this.resolvedAt = LocalDateTime.now();
    }
    
    public void reject(String adminComment) {
        this.status = ReportStatus.REJECTED;
        this.adminComment = adminComment;
        this.resolvedAt = LocalDateTime.now();
    }
}
