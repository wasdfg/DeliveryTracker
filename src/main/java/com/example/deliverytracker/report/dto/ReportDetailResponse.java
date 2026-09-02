package com.example.deliverytracker.report.dto;

import com.example.deliverytracker.report.entity.Report;
import com.example.deliverytracker.report.entity.ReportStatus;
import com.example.deliverytracker.report.entity.ReportType;
import com.example.deliverytracker.report.entity.TargetType;

import java.time.LocalDateTime;

public record ReportDetailResponse(Long id, Long reporterId, String reporterNickname, TargetType targetType,
                                        Long targetId, ReportType reportType, String description, ReportStatus status,
                                        String adminComment, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime resolvedAt) {

    public static ReportDetailResponse from(Report report) {

        return new ReportDetailResponse(
                report.getId(),
                report.getReporter().getId(),
                report.getReporter().getNickname(),
                report.getTargetType(),
                report.getTargetId(),
                report.getType(),
                report.getDescription(),
                report.getStatus(),
                report.getAdminComment(),
                report.getCreatedAt(),
                report.getUpdatedAt(),
                report.getResolvedAt()
        );
    }
}
