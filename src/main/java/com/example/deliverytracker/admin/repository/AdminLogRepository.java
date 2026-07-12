package com.example.deliverytracker.admin.repository;

import com.example.deliverytracker.admin.entity.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminLogRepository extends JpaRepository<AdminLog, Long> {
}
