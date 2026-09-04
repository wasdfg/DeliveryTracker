package com.example.deliverytracker.admin.service;

import com.example.deliverytracker.admin.dto.AdminOrderResponse;
import com.example.deliverytracker.admin.dto.AdminProductDetailResponse;
import com.example.deliverytracker.admin.dto.AdminProductResponse;
import com.example.deliverytracker.admin.dto.AdminProductSearchCondition;
import com.example.deliverytracker.admin.entity.AdminAction;
import com.example.deliverytracker.admin.entity.TargetType;
import com.example.deliverytracker.admin.repository.AdminProductRepository;
import com.example.deliverytracker.store.entity.Product;
import com.example.deliverytracker.store.repository.ProductRepository;
import com.example.deliverytracker.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;

    private final AdminProductRepository adminProductRepository;

    private final AdminLogService adminLogService;

    public Page<AdminProductResponse> getProducts(AdminProductSearchCondition condition, Pageable pageable) {

        return adminProductRepository.searchProducts(condition, pageable).map(AdminProductResponse::from);
    }

    public AdminProductDetailResponse getProduct(Long productId) {

        Product product = adminProductRepository.findAdminProduct(productId).orElseThrow(() -> new EntityNotFoundException("제품을 찾을 수 없습니다."));

        return AdminProductDetailResponse.from(product);
    }

    @Transactional
    public void changeProductStatus(Long productId, String reason, User admin) {

        Product product = this.productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("제품을 찾을 수 없습니다."));;

        boolean beforeValue = product.isAvailable();

        product.toggleAvailability();

        boolean afterValue = product.isAvailable();

        adminLogService.saveLog(admin, TargetType.PRODUCT, productId, AdminAction.STATUS_CHANGE, reason, String.valueOf(beforeValue), String.valueOf(afterValue));
    }

    @Transactional
    public void changeProductDeleted(Long productId, String reason, User admin) {

        Product product = this.productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("제품을 찾을 수 없습니다."));;

        if(!product.isDelete()){
            throw new IllegalArgumentException("이미 삭제된 제품입니다.");
        }

        boolean beforeValue = true;

        product.delete();

        boolean afterValue = product.isDelete();

        adminLogService.saveLog(admin, TargetType.PRODUCT, productId, AdminAction.DELETE, reason, String.valueOf(beforeValue), String.valueOf(afterValue));
    }

    @Transactional
    public void changeProductRestore(Long productId, String reason, User admin) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("제품을 찾을 수 없습니다."));;

        if(product.isDelete()){
            throw new IllegalArgumentException("이미 복구된 제품입니다.");
        }

        boolean beforeValue = false;

        product.restore();

        boolean afterValue = product.isDelete();

        adminLogService.saveLog(admin, TargetType.PRODUCT, productId, AdminAction.RESTORE, reason, String.valueOf(beforeValue), String.valueOf(afterValue));

    }
}
