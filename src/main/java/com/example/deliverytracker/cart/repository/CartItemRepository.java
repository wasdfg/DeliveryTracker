package com.example.deliverytracker.cart.repository;

import com.example.deliverytracker.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}