package com.example.deliverytracker.favorite.contorller;

import com.example.deliverytracker.favorite.service.FavoriteService;
import com.example.deliverytracker.store.dto.StoreDetailResponse;
import com.example.deliverytracker.store.dto.StoreResponse;
import com.example.deliverytracker.user.entitiy.User;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("/my")
    public ResponseEntity<List<StoreResponse>> getMyFavorites(@AuthenticationPrincipal UserDetailsImpl user) {
        List<StoreResponse> favorites = favoriteService.getMyFavorites(user.getUser());
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/{storeId}")
    public ResponseEntity<Void> addFavorite(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable Long storeId) {
        favoriteService.addFavorite(user.getUser(), storeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> removeFavorite(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable Long storeId) {
        favoriteService.removeFavorite(user.getUser(), storeId);
        return ResponseEntity.ok().build();
    }
}
