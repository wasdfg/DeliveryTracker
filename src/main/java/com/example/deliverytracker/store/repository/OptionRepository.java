package com.example.deliverytracker.store.repository;

import com.example.deliverytracker.store.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByIdIn(List<Long> ids);
}
