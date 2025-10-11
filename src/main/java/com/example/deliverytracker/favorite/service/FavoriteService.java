package com.example.deliverytracker.favorite.service;

import com.example.deliverytracker.favorite.entity.Favorite;
import com.example.deliverytracker.favorite.repository.FavoriteRepository;
import com.example.deliverytracker.store.dto.StoreDetailResponse;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.store.repository.StoreRepository;
import com.example.deliverytracker.user.entitiy.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final StoreRepository storeRepository;

    @Transactional
    public void addFavorite(User user, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

        favoriteRepository.findByUserAndStore(user, store).ifPresent(f -> {
            throw new DataIntegrityViolationException("이미 찜한 가게입니다.");
        });

        Favorite favorite = new Favorite(user, store);
        favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(User user, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

        Favorite favorite = favoriteRepository.findByUserAndStore(user, store)
                .orElseThrow(() -> new EntityNotFoundException("찜한 내역을 찾을 수 없습니다."));

        favoriteRepository.delete(favorite);
    }

    public List<StoreDetailResponse> getFavoriteStores(User user) {
        List<Favorite> favorites = favoriteRepository.findAllByUser(user);

        return favorites.stream()
                .map(Favorite::getStore) 
                .map(StoreDetailResponse::from)
                .collect(Collectors.toList());
    }
}
