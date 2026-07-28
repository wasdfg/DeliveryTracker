package com.example.deliverytracker.admin.controller;

import com.example.deliverytracker.admin.dto.AdminLogResponse;
import com.example.deliverytracker.admin.service.AdminLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/logs")
public class AdminLogController {

    private final AdminLogService adminLogService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<AdminLogResponse>> getLogs(Pageable pageable) {

        return ResponseEntity.ok(adminLogService.getLogs(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/recent")
    public ResponseEntity<List<AdminLogResponse>> getRecentLogs(@RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(adminLogService.getRecentLogs(size));
    }
}
