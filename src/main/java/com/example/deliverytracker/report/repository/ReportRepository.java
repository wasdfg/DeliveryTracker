package com.example.deliverytracker.report.repository;

import com.example.deliverytracker.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryCustom {
}
