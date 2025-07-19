package com.example.deliverytracker.delivery.repository;

import com.example.deliverytracker.delivery.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery,Long> {

    Page<Delivery> findByRequesterId(Long requesterId, Pageable pageable);
}
