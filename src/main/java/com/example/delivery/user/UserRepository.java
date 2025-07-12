package com.example.delivery.user;

import com.example.delivery.user.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);

    boolean existsByIdForLogin(String idForLogin);

    public Optional<User> findByIdForLogin(String idForLogin);
}
