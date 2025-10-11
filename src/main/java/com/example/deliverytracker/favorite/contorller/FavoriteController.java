package com.example.deliverytracker.favorite.contorller;

import com.example.deliverytracker.favorite.service.FavoriteService;
import com.example.deliverytracker.store.dto.StoreDetailResponse;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/stores/{storeId}/favorite")
    public ResponseEntity<String> addFavorite(
            @PathVariable Long storeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        favoriteService.addFavorite(userDetails.getUser(), storeId);
        return ResponseEntity.status(HttpStatus.CREATED).body("가게를 찜했습니다.");
    }

    @DeleteMapping("/stores/{storeId}/favorite")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable Long storeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        favoriteService.removeFavorite(userDetails.getUser(), storeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/me/favorites")
    public ResponseEntity<List<StoreDetailResponse>> getFavoriteStores(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<StoreDetailResponse> favoriteStores = favoriteService.getFavoriteStores(userDetails.getUser());
        return ResponseEntity.ok(favoriteStores);
    }
}
