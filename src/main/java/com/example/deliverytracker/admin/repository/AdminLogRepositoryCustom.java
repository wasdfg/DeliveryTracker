package com.example.deliverytracker.admin.repository;

import com.example.deliverytracker.admin.dto.AdminLogSearchCondition;
import com.example.deliverytracker.admin.entity.AdminLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminLogRepositoryCustom {

    Page<AdminLog> searchLogs(AdminLogSearchCondition condition, Pageable pageable);
}
