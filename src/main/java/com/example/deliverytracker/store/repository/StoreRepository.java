package com.example.deliverytracker.store.repository;

import com.example.deliverytracker.store.entity.Store;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByIdAndActiveTrue(Long id);

    @Query("SELECT s FROM Store s LEFT JOIN FETCH s.products WHERE s.id = :storeId")
    Optional<Store> findStoreWithProductsById(@Param("storeId") Long storeId);
}
