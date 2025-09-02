package com.example.deliverytracker.store.contorller;

import com.example.deliverytracker.store.dto.StoreDetailResponse;
import com.example.deliverytracker.store.dto.StoreRequest;
import com.example.deliverytracker.store.dto.StoreResponse;
import com.example.deliverytracker.store.dto.StoreSearchCondition;
import com.example.deliverytracker.store.service.StoreService;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
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

        StoreDetailResponse storeDetail = storeService.getStoreInfo(storeId);

        return ResponseEntity.ok(storeDetail);
    }

    @GetMapping("/")
    public ResponseEntity<Page<StoreDetailResponse>> getStoreList(Pageable pageable){

        Page<StoreDetailResponse> storeDetail = storeService.getStoreList(pageable);

        return ResponseEntity.ok(storeDetail);
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<?> changeStoreInfo(@PathVariable Long storeId,@RequestBody StoreRequest request,@AuthenticationPrincipal UserDetailsImpl userDetails){

        this.storeService.changeStoreInfo(storeId,request,userDetails.getUser());

        return ResponseEntity.ok("가게 정보가 변경되었습니다.");
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeId,@AuthenticationPrincipal UserDetailsImpl userDetails){

        this.storeService.deleteStore(storeId,userDetails.getUser());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<StoreResponse>> searchStore(@ModelAttribute StoreSearchCondition condition, Pageable pageable){

        Page<StoreResponse> results = storeService.searchStores(condition, pageable);

        return ResponseEntity.ok(results);
    }
}
