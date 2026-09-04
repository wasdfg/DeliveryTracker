package com.example.deliverytracker.report.controller;

import com.example.deliverytracker.report.dto.ReportCreateRequest;
import com.example.deliverytracker.report.dto.ReportDetailResponse;
import com.example.deliverytracker.report.dto.ReportProcessRequest;
import com.example.deliverytracker.report.dto.ReportResponse;
import com.example.deliverytracker.report.dto.ReportSearchCondition;
import com.example.deliverytracker.report.service.ReportService;
import com.example.deliverytracker.user.entity.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/report")
@RestController
public class ReportController {

    private final ReportService reportService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ReportResponse>> getReports(ReportSearchCondition condition, Pageable pageable) {

        return ResponseEntity.ok(reportService.getReports(condition, pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportDetailResponse> getReport(@PathVariable Long reportId) {

        return ResponseEntity.ok(reportService.getReport(reportId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{reportId}/start")
    public ResponseEntity<Void> startProcess(@PathVariable Long reportId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        reportService.startProcess(reportId, userDetails.getUser());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{reportId}/resolve")
    public ResponseEntity<Void> resolveReport(@PathVariable Long reportId, @Valid @RequestBody ReportProcessRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        reportService.resolveReport(reportId, request.getComment(), userDetails.getUser());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{reportId}/reject")
    public ResponseEntity<Void> rejectReport(@PathVariable Long reportId, @Valid @RequestBody ReportProcessRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        reportService.rejectReport(reportId, request.getComment(), userDetails.getUser());

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createReport(@Valid @RequestBody ReportCreateRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        reportService.createReport(request, userDetails.getUser());

        return ResponseEntity.ok().build();
    }

}
