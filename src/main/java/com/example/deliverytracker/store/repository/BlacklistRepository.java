package com.example.deliverytracker.store.repository;

import com.example.deliverytracker.store.entity.Blacklist;
import com.querydsl.core.Fetchable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {

    boolean existsByStoreIdAndUserId(Long storeId, Long userId);

    List<Blacklist> findAllByStoreIdOrderByCreatedAtDesc(Long storeId);

    void deleteByStoreIdAndUserId(Long storeId, Long userId);

}
