package com.example.deliverytracker.cart.entity;

import com.example.deliverytracker.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartOption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_id")
    private CartItem cartItem;

    private Long optionId;
    private String name;
    private BigDecimal price;

    @Builder
    public CartOption(CartItem cartItem, Long optionId, String name, BigDecimal price) {
        this.cartItem = cartItem;
        this.optionId = optionId;
        this.name = name;
        this.price = price;
    }
}
