package com.example.deliverytracker.store.contorller;

import com.example.deliverytracker.store.dto.StoreRequest;
import com.example.deliverytracker.store.dto.StoreResponse;
import com.example.deliverytracker.store.service.StoreService;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/stores")
@RestController
public class StoreContorller {

    private final StoreService storeService;

    @PostMapping("/")
    public ResponseEntity<String> registStore(@Valid @RequestBody StoreRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails){

        storeService.registStore(request, userDetails.getUser());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("가게 등록이 완료되었습니다.");
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<?> getStoreInfo(@PathVariable Long storeId){

        StoreResponse storeDetail = storeService.getStoreInfo(storeId);

        return ResponseEntity.ok(storeDetail);
    }
}
