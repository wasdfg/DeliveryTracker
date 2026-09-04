package com.example.deliverytracker.report.service;

import com.example.deliverytracker.admin.entity.AdminAction;
import com.example.deliverytracker.admin.entity.TargetType;
import com.example.deliverytracker.admin.service.AdminLogService;
import com.example.deliverytracker.report.dto.ReportCreateRequest;
import com.example.deliverytracker.report.dto.ReportDetailResponse;
import com.example.deliverytracker.report.dto.ReportResponse;
import com.example.deliverytracker.report.dto.ReportSearchCondition;
import com.example.deliverytracker.report.entity.Report;
import com.example.deliverytracker.report.entity.ReportStatus;
import com.example.deliverytracker.report.repository.ReportRepository;
import com.example.deliverytracker.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    private final AdminLogService adminLogService;

    public Page<ReportResponse> getReports(ReportSearchCondition condition, Pageable pageable){

        Page<Report> reports = reportRepository.searchReports(condition,pageable);

        return reports.map(ReportResponse::from);
    }

    public ReportDetailResponse getReport(Long reportId){

        Report report = reportRepository.findReport(reportId).orElseThrow(() -> new EntityNotFoundException("신고를 찾을 수 없습니다."));

        return ReportDetailResponse.from(report);
    }


    @Transactional
    public void startProcess(Long reportId, User admin){
        Report report = reportRepository.findReport(reportId).orElseThrow(() -> new EntityNotFoundException("신고를 찾을 수 없습니다."));

        ReportStatus beforeValue = report.getStatus();

        report.startProcess();

        ReportStatus afterValue = report.getStatus();

        adminLogService.saveLog(admin, TargetType.REPORT, reportId, AdminAction.REPORT_PROCESS_STARTED, "", String.valueOf(beforeValue), String.valueOf(afterValue));
    }


    @Transactional
    public void resolveReport(Long reportId, String adminComment, User admin){

        Report report = reportRepository.findReport(reportId).orElseThrow(() -> new EntityNotFoundException("신고를 찾을 수 없습니다."));

        ReportStatus beforeValue = report.getStatus();

        report.resolve(adminComment);

        ReportStatus afterValue = report.getStatus();

        adminLogService.saveLog(admin, TargetType.REPORT, reportId, AdminAction.REPORT_RESOLVED, adminComment, beforeValue.name(), afterValue.name());
    }


    @Transactional
    public void rejectReport(Long reportId, String adminComment, User admin){

        Report report = reportRepository.findReport(reportId).orElseThrow(() -> new EntityNotFoundException("신고를 찾을 수 없습니다."));

        ReportStatus beforeValue = report.getStatus();

        report.reject(adminComment);

        ReportStatus afterValue = report.getStatus();

        adminLogService.saveLog(admin, TargetType.REPORT, reportId, AdminAction.REPORT_REJECTED, adminComment, beforeValue.name(), afterValue.name());

    }

    @Transactional
    public void createReport(ReportCreateRequest request, User reporter) {

        Report report = new Report(reporter, request.getTargetType(), request.getTargetId(), request.getType(), request.getDescription());

        reportRepository.save(report);
    }
}
