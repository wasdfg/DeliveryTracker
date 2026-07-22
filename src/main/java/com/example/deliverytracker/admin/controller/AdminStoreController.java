package com.example.deliverytracker.admin.controller;

import com.example.deliverytracker.admin.dto.AdminStoreSearchCondition;
import com.example.deliverytracker.admin.dto.StoreAdminResponse;
import com.example.deliverytracker.admin.service.AdminStoreService;
import com.example.deliverytracker.store.dto.StoreStatusRequest;
import com.example.deliverytracker.user.entity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminStoreController {

    private final AdminStoreService adminStoreService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stores")
    public ResponseEntity<Page<StoreAdminResponse>> getStores(@ModelAttribute AdminStoreSearchCondition condition, Pageable pageable){

        Page<StoreAdminResponse> stores = adminStoreService.getStores(condition,pageable);

        return ResponseEntity.ok(stores);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/stores/{storeId}/status")
    public ResponseEntity<Void> updateStoreStatus(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long storeId, @RequestBody StoreStatusRequest request) {

        adminStoreService.updateStoreStatus(userDetails.getUser(),storeId, request);

        return ResponseEntity.noContent().build();
    }
}
