package com.example.deliverytracker.store.repository;

import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.store.entity.StoreCategory;
import com.example.deliverytracker.user.entitiy.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  StoreRepository extends JpaRepository<Store, Long>{
    Optional<Store> findByIdAndActiveTrue(Long id);

    @Query("SELECT s FROM Store s LEFT JOIN FETCH s.products WHERE s.id = :storeId")
    Optional<Store> findStoreWithProductsById(@Param("storeId") Long storeId);

    Page<Store> findStoreList(Pageable pageable);

    Page<Store> findByNameContaining(String keyword, Pageable pageable);

    Page<Store> findByCategory(StoreCategory category, Pageable pageable);

    Page<Store> findByNameContainingAndCategory(String keyword, StoreCategory category, Pageable pageable);

    Optional<Store> findByOwner(User owner);
}
