package com.example.deliverytracker.rider.entity;

import com.example.deliverytracker.user.entitiy.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "riders", uniqueConstraints = {
        @UniqueConstraint(name = "UK_rider_email", columnNames = "email")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Rider {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    // 실시간 위치 정보 갱신용
    private Double currentLat;
    private Double currentLng;

    private LocalDateTime updatedAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @PrePersist //저장되기 직선에 사용 persist직전
    public void prePersist() {
        if (this.status == null) this.status = Status.WAITING;
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate //jpa가 dirty checking할때 사용
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum Status {
        WAITING,    // 대기 중
        ACTIVE,     // 배달 중
        OFFLINE     // 오프라인 (로그아웃 또는 미접속)
    }
}
