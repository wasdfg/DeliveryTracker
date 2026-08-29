package com.example.deliverytracker.admin.controller;

import com.example.deliverytracker.admin.dto.AdminProductDetailResponse;
import com.example.deliverytracker.admin.dto.AdminProductReasonRequest;
import com.example.deliverytracker.admin.dto.AdminProductResponse;
import com.example.deliverytracker.admin.dto.AdminProductSearchCondition;
import com.example.deliverytracker.admin.service.AdminProductService;
import com.example.deliverytracker.user.entity.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final AdminProductService adminProductService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<AdminProductResponse>> getProducts(AdminProductSearchCondition condition, Pageable pageable) {

        Page<AdminProductResponse> products = adminProductService.getProducts(condition, pageable);

        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{productId}")
    public ResponseEntity<AdminProductDetailResponse> getProduct(@PathVariable Long productId) {

        AdminProductDetailResponse product = adminProductService.getProduct(productId);

        return ResponseEntity.ok(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{productId}/status")
    public ResponseEntity<Void> changeProductStatus(@PathVariable Long productId, @Valid @RequestBody AdminProductReasonRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        this.adminProductService.changeProductStatus(productId, request.getReason(), userDetails.getUser());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{productId}/delete")
    public ResponseEntity<Void> changeProductDeleted(@PathVariable Long productId, @Valid @RequestBody AdminProductReasonRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        this.adminProductService.changeProductDeleted(productId, request.getReason(), userDetails.getUser());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{productId}/restore")
    public ResponseEntity<Void> changeProductRestore(@PathVariable Long productId, @Valid @RequestBody AdminProductReasonRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        this.adminProductService.changeProductRestore(productId, request.getReason(), userDetails.getUser());

        return ResponseEntity.ok().build();
    }
}