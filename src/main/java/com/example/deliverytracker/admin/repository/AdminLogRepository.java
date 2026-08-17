package com.example.deliverytracker.admin.repository;

import com.example.deliverytracker.admin.entity.AdminLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminLogRepository extends JpaRepository<AdminLog, Long>, AdminLogRepositoryCustom{

    Page<AdminLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
