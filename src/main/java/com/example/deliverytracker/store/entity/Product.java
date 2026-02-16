package com.example.deliverytracker.store.entity;


import com.example.deliverytracker.common.BaseEntity;
import com.example.deliverytracker.store.dto.ProductUpdateRequest;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private BigDecimal price;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(nullable = true)
    private Integer stock;

    private String imageUrl;

    @Column(nullable = false)
    private boolean isAvailable = true;

    @Column(nullable = false)
    private boolean isDelete = true;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionGroup> optionGroups = new ArrayList<>();

    @Builder
    public Product(String name, BigDecimal price, String description, ProductCategory category, int stock, String imageUrl, boolean isAvailable,Store store) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.isAvailable = isAvailable;
        this.store = store;
    }

    public void updateInfo(ProductUpdateRequest request, String imageUrl){
        if(request.getCategory() != null){
            this.category = request.getCategory();
        }

        if(request.getDescription() != null){
            this.description = request.getDescription();
        }

        if(request.getName() != null){
            this.name = request.getName();
        }

        if(request.getPrice() != null){
            this.price = BigDecimal.valueOf(request.getPrice());
        }

        if(request.getStock() != null){
            this.stock = request.getStock();
        }

        this.imageUrl = imageUrl;
    }

    public void delete() {
        this.isDelete = true;
    }

    public void toggleAvailability() {
        this.isAvailable = !this.isAvailable;
    }
}

