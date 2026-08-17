package com.example.deliverytracker.admin.service;

import com.example.deliverytracker.admin.dto.AdminLogResponse;
import com.example.deliverytracker.admin.dto.AdminLogSearchCondition;
import com.example.deliverytracker.admin.entity.AdminAction;
import com.example.deliverytracker.admin.entity.AdminLog;
import com.example.deliverytracker.admin.entity.TargetType;
import com.example.deliverytracker.admin.repository.AdminLogRepository;
import com.example.deliverytracker.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminLogService {

    private final AdminLogRepository adminLogRepository;

    @Transactional
    public void saveLog(User admin, TargetType targetType, Long targetId, AdminAction action, String description, String beforeValue, String afterValue){

        AdminLog adminLog = AdminLog.of(admin, targetType, targetId, action, description, beforeValue, afterValue);

        this.adminLogRepository.save(adminLog);
    }

    public Page<AdminLogResponse> getLogs(AdminLogSearchCondition condition, Pageable pageable) {

        return adminLogRepository.searchLogs(condition, pageable).map(AdminLogResponse::from);
    }

    public List<AdminLogResponse> getRecentLogs(int size) {

        Pageable pageable = PageRequest.of(0, size);

        return adminLogRepository.findAllByOrderByCreatedAtDesc(pageable).getContent().stream().map(AdminLogResponse::from).toList();
    }
}
