package com.example.deliverytracker.user.repository;

import com.example.deliverytracker.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>, UserRepositoryCustom  {
    boolean existsByEmail(String email);

    boolean existsByIdForLogin(String idForLogin);

    Optional<User> findByIdForLogin(String idForLogin);

    Optional<User> findByEmail(String email);

    List<User> findByStatusAndWithdrawnAtBefore(User.Status status, LocalDateTime withdrawnAt);

    Page<User> findByEmailContainingOrNicknameContaining(String email, String nickname, Pageable pageable);
}
