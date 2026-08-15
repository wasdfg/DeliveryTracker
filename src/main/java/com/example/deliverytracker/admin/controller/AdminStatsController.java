package com.example.deliverytracker.admin.controller;

import com.example.deliverytracker.admin.dto.AdminStatsResponse;
import com.example.deliverytracker.admin.service.AdminStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getStats() {

        return ResponseEntity.ok(adminStatsService.getStats());
    }
}
