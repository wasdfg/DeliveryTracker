package com.example.deliverytracker.store.repository;

import com.example.deliverytracker.store.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
