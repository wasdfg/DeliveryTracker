package com.example.deliverytracker.store.repository;

import com.example.deliverytracker.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByIdAndActiveTrue(Long id);
}
