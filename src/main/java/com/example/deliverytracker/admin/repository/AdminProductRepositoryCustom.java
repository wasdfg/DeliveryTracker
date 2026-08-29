package com.example.deliverytracker.admin.repository;

import com.example.deliverytracker.admin.dto.AdminProductSearchCondition;
import com.example.deliverytracker.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AdminProductRepositoryCustom {
    Page<Product> searchProducts(AdminProductSearchCondition condition, Pageable pageable);

    Optional<Product> findAdminProduct(Long productId);
}
