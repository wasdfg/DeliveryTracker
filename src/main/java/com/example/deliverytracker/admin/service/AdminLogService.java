package com.example.deliverytracker.admin.service;

import com.example.deliverytracker.admin.entity.AdminAction;
import com.example.deliverytracker.admin.entity.AdminLog;
import com.example.deliverytracker.admin.entity.TargetType;
import com.example.deliverytracker.admin.repository.AdminLogRepository;
import com.example.deliverytracker.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminLogService {

    private final AdminLogRepository adminLogRepository;

    @Transactional
    public void saveLog(User admin, TargetType targetType, Long targetId, AdminAction action, String description, String beforeValue, String afterValue){

        AdminLog adminLog = new AdminLog();

        this.adminLogRepository.save(adminLog);
    }
}
