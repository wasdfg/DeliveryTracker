package com.example.deliverytracker.admin.controller;

import com.example.deliverytracker.admin.dto.AdminStoreSearchCondition;
import com.example.deliverytracker.admin.dto.StoreAdminResponse;
import com.example.deliverytracker.admin.dto.UserSearchCondition;
import com.example.deliverytracker.admin.dto.UserStatusRequest;
import com.example.deliverytracker.admin.service.AdminUserService;
import com.example.deliverytracker.store.dto.StoreResponse;
import com.example.deliverytracker.store.dto.StoreSearchCondition;
import com.example.deliverytracker.store.dto.StoreStatusRequest;
import com.example.deliverytracker.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getAllUserInfo(UserSearchCondition condition, Pageable pageable) {

        Page<UserResponse> response = adminUserService.getAllUserInfo(condition, pageable);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserInfo(@PathVariable Long userId) {

        UserResponse response = adminUserService.getUserInfo(userId);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<Void> updateUserStatus(@PathVariable Long userId, UserStatusRequest userStatusRequest) {

        adminUserService.updateUserStatus(userId,userStatusRequest.getStatus());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stores")
    public ResponseEntity<Page<StoreAdminResponse>> getStores(@ModelAttribute AdminStoreSearchCondition condition, Pageable pageable){

        Page<StoreAdminResponse> stores = adminUserService.getStores(condition,pageable);

        return ResponseEntity.ok(stores);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/stores/{storeId}/status")
    public ResponseEntity<Void> updateStoreStatus(@PathVariable Long storeId, @RequestBody StoreStatusRequest request) {

        adminUserService.updateStoreStatus(storeId, request);

        return ResponseEntity.noContent().build();
    }

}
