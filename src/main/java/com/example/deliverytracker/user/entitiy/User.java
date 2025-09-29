package com.example.deliverytracker.user.entitiy;

import com.example.deliverytracker.common.BaseEntity;
import com.example.deliverytracker.review.entity.Review;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "UK_user_email", columnNames = "email"),
        @UniqueConstraint(name = "UK_user_login_id", columnNames = "id_for_login")
})
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(name = "id_for_login", nullable = false, length = 30)
    private String idForLogin;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(length = 20)
    @Pattern(regexp = "^(010|011)-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phone;

    @Column(length = 1000)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    private String imageUrl;

    public enum Role {
        USER, ADMIN , RIDER , STORE_OWNER
    }

    public enum Status{
        ACTIVE,       // 정상 활성 상태
        INACTIVE,     // 이메일 미인증 또는 비활성화 계정
        SUSPENDED,    // 관리자에 의해 정지됨
        WITHDRAWN     // 회원 탈퇴한 사용자
    }

    public void changeInfo(String email, String nickname, String phone, String address,String imageUrl) {
        this.email = email;
        this.nickname = nickname;
        this.phone = phone;
        this.address = address;
        this.imageUrl = imageUrl;
    }

    public void changePassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }
}
