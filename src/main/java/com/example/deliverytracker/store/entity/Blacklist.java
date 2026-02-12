package com.example.deliverytracker.store.entity;

import com.example.deliverytracker.common.BaseEntity;
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
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"store_id", "user_id"}) // 중복 차단 방지
})
public class Blacklist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = true)
    private String reason;

    public Blacklist(Store store, User user, String reason) {
        this.store = store;
        this.user = user;
        this.reason = reason;
    }

    public static Blacklist createBlacklist(Store store, User user, String reason) {
        if (store == null || user == null) {
            throw new IllegalArgumentException("가게 정보와 유저 정보는 필수입니다.");
        }
        return new Blacklist(store, user, reason);
    }
}
