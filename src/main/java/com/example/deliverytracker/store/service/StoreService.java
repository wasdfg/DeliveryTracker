package com.example.deliverytracker.store.service;

import com.example.deliverytracker.order.dto.OrderForOwnerResponse;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.repository.OrderRepository;
import com.example.deliverytracker.store.dto.StoreDetailResponse;
import com.example.deliverytracker.store.dto.StoreRequest;
import com.example.deliverytracker.store.dto.StoreResponse;
import com.example.deliverytracker.store.dto.StoreSearchCondition;
import com.example.deliverytracker.store.entity.Category;
import com.example.deliverytracker.store.entity.DeliveryTime;
import com.example.deliverytracker.store.entity.OperationTime;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.store.repository.CategoryRepository;
import com.example.deliverytracker.store.repository.StoreRepository;
import com.example.deliverytracker.user.entitiy.User;
import com.example.deliverytracker.image.service.ImageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    private final OrderRepository orderRepository;

    private final CategoryRepository categoryRepository;

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

        Long categoryId = request.getCategoryId();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카테고리를 찾을 수 없습니다. ID: " + categoryId));

        Store store = Store.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .active(true)
                .owner(user)
                .description(request.getDescription())
                .category(category)
                .minOrderAmount(request.getMinOrderAmount())
                .deliveryFee(request.getDeliveryFee())
                .imageUrl(imageUrl)
                .isManualClosed(false)
                .build();

        if (request.getOperationTimes() != null) {
            for (StoreRequest.OperationTimeRequest timeRequest : request.getOperationTimes()) {
                OperationTime operationTime = OperationTime.builder()
                        .dayOfWeek(timeRequest.getDayOfWeek())
                        .openTime(LocalTime.parse(timeRequest.getOpenTime())) // String -> LocalTime 변환
                        .closeTime(LocalTime.parse(timeRequest.getCloseTime()))
                        .isDayOff(timeRequest.isDayOff())
                        .store(store)
                        .build();

                store.getOperationTimes().add(operationTime);
            }
        }

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

        if (request.getOperationTimes() != null && !request.getOperationTimes().isEmpty()) {

            store.getOperationTimes().clear();

            for (StoreRequest.OperationTimeRequest timeReq : request.getOperationTimes()) {
                OperationTime newTime = OperationTime.builder()
                        .dayOfWeek(timeReq.getDayOfWeek())
                        .openTime(LocalTime.parse(timeReq.getOpenTime()))
                        .closeTime(LocalTime.parse(timeReq.getCloseTime()))
                        .isDayOff(timeReq.isDayOff())
                        .store(store)
                        .build();
                store.getOperationTimes().add(newTime);
            }
        }

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

    public Page<StoreResponse> searchStores(StoreSearchCondition condition, Pageable pageable) {
        return storeRepository.searchStores(condition, pageable).map(StoreResponse::from);
    }

    public Page<OrderForOwnerResponse> getOrdersForMyStore(User user, Pageable pageable){

        Store store = this.storeRepository.findByOwner(user)
                .orElseThrow(() -> new EntityNotFoundException("소유한 가게 정보가 없습니다."));

        Page<Order> orderPage = orderRepository.findByStore(store, pageable);

        return orderPage.map(OrderForOwnerResponse::from);
    }

    @Transactional
    public void updateDeliveryTime(Long storeId, User owner, DeliveryTime newTime) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException("가게 정보를 수정할 권한이 없습니다.");
        }

        store.changeCurrentDeliveryTime(newTime);
    }
}
