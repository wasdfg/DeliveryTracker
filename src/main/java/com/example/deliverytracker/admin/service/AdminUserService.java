package com.example.deliverytracker.admin.service;

import com.example.deliverytracker.admin.dto.AdminStoreSearchCondition;
import com.example.deliverytracker.admin.dto.StoreAdminResponse;
import com.example.deliverytracker.admin.dto.UserSearchCondition;
import com.example.deliverytracker.store.dto.StoreResponse;
import com.example.deliverytracker.store.dto.StoreSearchCondition;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.store.repository.StoreRepository;
import com.example.deliverytracker.user.dto.UserResponse;
import com.example.deliverytracker.user.entity.User;
import com.example.deliverytracker.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    private final StoreRepository storeRepository;

    public Page<UserResponse> getAllUserInfo(UserSearchCondition condition, Pageable pageable){

        Page<User> page = this.userRepository.searchUsers(condition, pageable);

        return page.map(UserResponse::new);
    }

    public UserResponse getUserInfo(Long userId){
        User user = userRepository.getReferenceById(userId);

        return new UserResponse(user);
    }

    @Transactional
    public void updateUserStatus(Long userId, User.Status status){
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("유저가 없습니다."));

        switch (status) {

            case WITHDRAWN -> user.withdraw();

            case ACTIVE -> user.restore();

            default -> user.changeStatus(status);
        }
    }

    public Page<StoreAdminResponse> getStores(AdminStoreSearchCondition condition, Pageable pageable){
        Page<Store> stores = storeRepository.searchStoresForAdmin(condition,pageable);

        return stores.map(StoreAdminResponse::from);
    }
}
