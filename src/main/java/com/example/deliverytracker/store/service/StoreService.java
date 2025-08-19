package com.example.deliverytracker.store.service;

import com.example.deliverytracker.store.dto.StoreDetailResponse;
import com.example.deliverytracker.store.dto.StoreRequest;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.store.repository.StoreRepository;
import com.example.deliverytracker.user.entitiy.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public void registStore(StoreRequest request, User user){
        if(!user.getRole().equals(User.Role.STORE_OWNER)){
            throw new AccessDeniedException("점장만 가게를 등록할 수 있습니다.");
        }

        Store store = Store.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .active(true)
                .owner(user)
                .description(request.getDescription())
                .category(request.getCategory())
                .operatingHours(request.getOperatingHours())
                .minOrderAmount(request.getMinOrderAmount())
                .deliveryFee(request.getDeliveryFee())
                .build();

        storeRepository.save(store);
        log.info("New store created storeId {}", store.getId());
    }

    public StoreDetailResponse getStoreInfo(Long storeId){

        Store store = this.storeRepository.findStoreWithProductsById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("가게 정보가 없습니다."));

        return StoreDetailResponse.from(store);
    }

    public Page<StoreDetailResponse> getStoreList(Pageable pageable){

        Page<Store> page = this.storeRepository.findStoreList(pageable);

        return page.map(StoreDetailResponse::from);
    }

    @Transactional
    public void changeStoreInfo(Long storeId, StoreRequest request, User user){

        Store store = this.storeRepository.findStoreWithProductsById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("가게 정보가 없습니다."));

        if (!store.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        store.changeInfo(request);

    }

    @Transactional
    public void deleteStore(Long storeId,User user){
        Store store = this.storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("가게 정보가 없습니다."));

        if (!store.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        store.delete(true);
    }
}
