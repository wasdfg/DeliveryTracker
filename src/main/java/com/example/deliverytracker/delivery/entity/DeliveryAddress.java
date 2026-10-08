package com.example.deliverytracker.delivery.entity;

import com.example.deliverytracker.common.BaseEntity;
import com.example.deliverytracker.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 30)
    private String receiverName;

    @Column(nullable = false, length = 20)
    private String receiverPhone;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(length = 500)
    private String detailAddress;

    private Double latitude;

    private Double longitude;

    @Column(nullable = false)
    private boolean defaultAddress;
}