package com.example.deliverytracker.store.service;

import com.example.deliverytracker.order.dto.OrderForOwnerResponse;
import com.example.deliverytracker.order.dto.OrderResponse;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.repository.OrderRepository;
import com.example.deliverytracker.store.dto.StoreDetailResponse;
import com.example.deliverytracker.store.dto.StoreRequest;
import com.example.deliverytracker.store.dto.StoreResponse;
import com.example.deliverytracker.store.dto.StoreSearchCondition;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.store.entity.StoreCategory;
import com.example.deliverytracker.store.repository.StoreRepository;
import com.example.deliverytracker.user.entitiy.User;
import com.example.image.service.ImageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    private final OrderRepository orderRepository;

    private final ImageService imageService;

    @Transactional
    public void registStore(StoreRequest request, User user, MultipartFile imageFile){
        if(!user.getRole().equals(User.Role.STORE_OWNER)){
            throw new AccessDeniedException("점장만 가게를 등록할 수 있습니다.");
        }

        String imageUrl = null;

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                imageUrl = imageService.upload(imageFile);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드에 실패했습니다.", e);
            }
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
                .imageUrl(imageUrl)
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

        Page<Store> page = this.storeRepository.findAll(pageable);

        return page.map(StoreDetailResponse::from);
    }

    @Transactional
    public void changeStoreInfo(Long storeId, StoreRequest request, User user,MultipartFile imageFile){

        Store store = this.storeRepository.findStoreWithProductsById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("가게 정보가 없습니다."));

        if (!store.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        String newImageUrl = store.getImageUrl();

        if (imageFile != null && !imageFile.isEmpty()) {
            try {

                if (store.getImageUrl() != null) {
                    imageService.delete(store.getImageUrl());
                }

                newImageUrl = imageService.upload(imageFile);
            } catch (IOException e) {
                throw new RuntimeException("이미지 수정에 실패했습니다.", e);
            }

        }
        else if (request.getDeleteImage() != null && request.getDeleteImage()) {
            if (store.getImageUrl() != null) {
                imageService.delete(store.getImageUrl());
            }

            newImageUrl = null;
        }

        store.changeInfo(request, newImageUrl);

    }

    @Transactional
    public void deleteStore(Long storeId,User user){
        Store store = this.storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("가게 정보가 없습니다."));

        if (!store.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        imageService.delete(store.getImageUrl());

        store.delete(true);

    }

    public Page<StoreResponse> searchStores(StoreSearchCondition condition,Pageable pageable){

        String keyword = condition.getKeyword();
        StoreCategory category = condition.getCategory();

        Page<Store> storePage;

        if (StringUtils.hasText(keyword) && category != null) {
            // 조건 : 이름 + 카테고리
            storePage = storeRepository.findByNameContainingAndCategory(keyword, category, pageable);
        } else if (StringUtils.hasText(keyword)) {
            // 조건 : 이름
            storePage = storeRepository.findByNameContaining(keyword, pageable);
        } else if (category != null) {
            // 조건 : 카테고리
            storePage = storeRepository.findByCategory(category, pageable);
        } else {

            storePage = storeRepository.findAll(pageable);
        }

        return storePage.map(StoreResponse::from);
    }

    public Page<OrderForOwnerResponse> getOrdersForMyStore(User user, Pageable pageable){

        Store store = this.storeRepository.findByOwner(user)
                .orElseThrow(() -> new EntityNotFoundException("소유한 가게 정보가 없습니다."));

        Page<Order> orderPage = orderRepository.findByStore(store, pageable);

        return orderPage.map(OrderForOwnerResponse::from);
    }
}
