package com.example.deliverytracker.favorite.repository;

import com.example.deliverytracker.favorite.entity.Favorite;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.user.entitiy.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserAndStore(User user, Store store);

    @Query("SELECT f FROM Favorite f JOIN FETCH f.store WHERE f.user.id = :userId")
    List<Favorite> findAllByUserIdWithStore(@Param("userId") Long userId);
}
