package com.example.deliverytracker.store.entity;

import com.example.deliverytracker.store.dto.StoreRequest;
import com.example.deliverytracker.user.entitiy.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String phone;

    private boolean active = true;

    private boolean isDeleted = false;

    private String description;

    @Enumerated(EnumType.STRING)
    private StoreCategory category;

    private String imageUrl;

    private String operatingHours;

    private BigDecimal minOrderAmount;

    private int deliveryFee;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    @Builder
    public Store(String name, String address, String phone, boolean active, User owner, String description, StoreCategory category, String operatingHours, BigDecimal minOrderAmount, int deliveryFee) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.active = active;
        this.owner = owner;
        this.description = description;
        this.category = category;
        this.operatingHours = operatingHours;
        this.minOrderAmount = minOrderAmount;
        this.deliveryFee = deliveryFee;
    }

    public void changeInfo(StoreRequest request) {
        if (request.getName() != null) {
            this.name = request.getName();
        }
        if (request.getAddress() != null) {
            this.address = request.getAddress();
        }
        if (request.getPhone() != null) {
            this.phone = request.getPhone();
        }
        if (request.getDescription() != null) {
            this.description = request.getDescription();
        }
        if (request.getOperatingHours() != null) {
            this.operatingHours = request.getOperatingHours();
        }
        if (request.getMinOrderAmount() != null) {
            this.minOrderAmount = request.getMinOrderAmount();
        }
        this.deliveryFee = request.getDeliveryFee();
        if (request.getCategory() != null) {
            this.category = request.getCategory();
        }
    }

    public void delete(boolean isDeleted){
        this.isDeleted = isDeleted;
    }

    public void addProduct(Product product) {
        this.products.add(product);
        product.setStore(this);
    }

}
