package com.example.deliverytracker.address.enitity;

import com.example.deliverytracker.user.entitiy.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String alias; // 주소 별명 (예: "집", "회사")

    @Column(nullable = false)
    private String streetAddress; // 도로명 주소

    private String detailAddress; // 상세 주소

    public Address(User user, String alias, String streetAddress, String detailAddress) {
        this.user = user;
        this.alias = alias;
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress;
    }

    public void update(String alias, String streetAddress, String detailAddress) {
        this.alias = alias;
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress;
    }
}