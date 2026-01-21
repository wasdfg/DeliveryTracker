package com.example.deliverytracker.order.contorller;

import com.example.deliverytracker.order.service.OwnerStatsService;
import com.example.deliverytracker.order.dto.OwnerStatsResponseDto;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/owner/stats")
@RequiredArgsConstructor
public class OwnerStatsController {

    private final OwnerStatsService ownerStatsService;

    @GetMapping("/{storeId}")
    public ResponseEntity<OwnerStatsResponseDto> getStoreStats(
            @PathVariable Long storeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        OwnerStatsResponseDto result = ownerStatsService.getStats(storeId, userDetails.getUser());

        return ResponseEntity.ok(result);
    }

}