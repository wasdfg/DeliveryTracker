package com.example.deliverytracker.report.repository;

import com.example.deliverytracker.report.dto.ReportSearchCondition;
import com.example.deliverytracker.report.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReportRepositoryCustom {

    Page<Report> searchReports(ReportSearchCondition condition, Pageable pageable);

    Optional<Report> findReport(Long reportId);
}
