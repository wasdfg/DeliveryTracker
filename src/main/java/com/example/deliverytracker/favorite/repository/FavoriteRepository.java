package com.example.deliverytracker.favorite.repository;

import com.example.deliverytracker.favorite.entity.Favorite;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.user.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserAndStore(User user, Store store);

    List<Favorite> findAllByUser(User user);
}
