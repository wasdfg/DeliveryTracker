package com.example.deliverytracker.user.repository;

import com.example.deliverytracker.user.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);

    boolean existsByIdForLogin(String idForLogin);

    Optional<User> findByIdForLogin(String idForLogin);

    Optional<User> findByEmail(String email);

    List<User> findByStatusAndWithdrawnAtBefore(User.Status status, LocalDateTime withdrawnAt);
}
