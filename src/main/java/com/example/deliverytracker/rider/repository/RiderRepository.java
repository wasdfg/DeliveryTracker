package com.example.deliverytracker.rider.repository;

import com.example.deliverytracker.rider.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepository extends JpaRepository<Rider,Long> {
}
