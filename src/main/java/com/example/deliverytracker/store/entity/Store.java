package com.example.deliverytracker.store.entity;

import com.example.deliverytracker.review.entity.Review;
import com.example.deliverytracker.store.dto.StoreRequest;
import com.example.deliverytracker.user.entitiy.User;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String imageUrl;

    private String operatingHours;

    private BigDecimal minOrderAmount;

    private int deliveryFee;

    @Column(name = "current_prep_time")
    private Integer currentPrepTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_delivery_time")
    private DeliveryTime currentDeliveryTime;

    private double averageRating = 0.0;

    private int reviewCount = 0;

    @Column(nullable = false)
    private boolean isManualClosed = false;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperationTime> operationTimes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Store(String name, String address, String phone, boolean active, User owner, String description, Category category, BigDecimal minOrderAmount, int deliveryFee, String imageUrl,boolean isManualClosed) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.active = active;
        this.owner = owner;
        this.description = description;
        this.category = category;
        this.minOrderAmount = minOrderAmount;
        this.deliveryFee = deliveryFee;
        this.imageUrl = imageUrl;
        this.isManualClosed = isManualClosed;
    }

    public void changeInfo(StoreRequest request,String newImageUrl) {
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
        if (request.getMinOrderAmount() != null) {
            this.minOrderAmount = request.getMinOrderAmount();
        }
        this.deliveryFee = request.getDeliveryFee();
        this.imageUrl = newImageUrl;
    }

    public void delete(boolean isDeleted){
        this.isDeleted = isDeleted;
    }

    public void updatePrepTime(Integer prepTime) {
        this.currentPrepTime = prepTime;
    }

    public void changeCurrentDeliveryTime(DeliveryTime newTime) {
        this.currentDeliveryTime = newTime;
    }

    public void addReview(double newRating) {
        this.averageRating = ((this.averageRating * this.reviewCount) + newRating) / (this.reviewCount + 1);
        this.reviewCount++;
    }

    public void toggleManualClose() {
        this.isManualClosed = !this.isManualClosed;
    }

    public boolean isCurrentlyOrderable() {
        if (this.isManualClosed) {
            return false;
        }

        return isWithinBusinessHours();
    }

    private boolean isWithinBusinessHours() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        LocalTime now = LocalTime.now();

        return operationTimes.stream()
                .filter(ot -> ot.getDayOfWeek() == today)
                .findFirst()
                .map(ot -> !ot.isDayOff() && !now.isBefore(ot.getOpenTime()) && !now.isAfter(ot.getCloseTime()))
                .orElse(false);
    }
}
