package com.example.deliverytracker.store.repository;

import com.example.deliverytracker.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndStoreId(Long productId, Long id);

    @EntityGraph(attributePaths = {"optionGroups", "optionGroups.options"})
    List<Product> findByStoreId(Long storeId);

    Page<Product> findListByStoreId(Long storeId, Pageable pageable);
}
