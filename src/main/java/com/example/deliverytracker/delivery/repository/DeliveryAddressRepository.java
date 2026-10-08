package com.example.deliverytracker.delivery.repository;

import com.example.deliverytracker.delivery.entity.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {

    List<DeliveryAddress> findAllByUserIdOrderByDefaultAddressDescIdDesc(Long userId);

    Optional<DeliveryAddress> findByIdAndUserId(Long id, Long userId);

    Optional<DeliveryAddress> findByUserIdAndDefaultAddressTrue(Long userId);
}