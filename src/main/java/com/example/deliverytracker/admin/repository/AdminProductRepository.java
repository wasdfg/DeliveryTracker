package com.example.deliverytracker.admin.repository;

import com.example.deliverytracker.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductRepository extends JpaRepository<Product, Long>, AdminProductRepositoryCustom{
}
