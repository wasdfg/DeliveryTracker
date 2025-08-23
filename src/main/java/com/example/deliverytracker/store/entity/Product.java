package com.example.deliverytracker.store.entity;


import com.example.deliverytracker.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private int stock;

    private String imageUrl;

    @Column(nullable = false)
    private boolean isAvailable = true;

    @Builder
    public Product(String name, int price, String description, ProductCategory category, int stock, String imageUrl, boolean isAvailable) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.isAvailable = isAvailable;
    }

    protected void setStore(Store store) {
        this.store = store;
    }
}

