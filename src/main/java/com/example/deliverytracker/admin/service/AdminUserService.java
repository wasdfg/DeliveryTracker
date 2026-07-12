package com.example.deliverytracker.admin.service;

import com.example.deliverytracker.admin.dto.AdminStoreSearchCondition;
import com.example.deliverytracker.admin.dto.StoreAdminResponse;
import com.example.deliverytracker.admin.dto.UserSearchCondition;
import com.example.deliverytracker.admin.entity.AdminAction;
import com.example.deliverytracker.admin.entity.TargetType;
import com.example.deliverytracker.store.dto.StoreStatusRequest;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.store.repository.StoreRepository;
import com.example.deliverytracker.user.dto.UserResponse;
import com.example.deliverytracker.user.entity.User;
import com.example.deliverytracker.user.entity.UserDetailsImpl;
import com.example.deliverytracker.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    private final StoreRepository storeRepository;

    private final AdminLogService adminLogService;

    public Page<UserResponse> getAllUserInfo(UserSearchCondition condition, Pageable pageable){

        Page<User> page = this.userRepository.searchUsers(condition, pageable);

        return page.map(UserResponse::new);
    }

    public UserResponse getUserInfo(Long userId){
        User user = userRepository.getReferenceById(userId);

        return new UserResponse(user);
    }

    @Transactional
    public void updateUserStatus(User user, Long userId, User.Status status){

        User.Status beforeStatus = user.getStatus();

        switch (status) {

            case WITHDRAWN -> user.withdraw();

            case ACTIVE -> user.restore();

            default -> user.changeStatus(status);
        }

        User.Status afterStatus = user.getStatus();

        adminLogService.saveLog(user, TargetType.USER, user.getId(), AdminAction.USER_STATUS_CHANGED, "회원 상태 변경", beforeStatus.name(), afterStatus.name());
    }

    public Page<StoreAdminResponse> getStores(AdminStoreSearchCondition condition, Pageable pageable){
        Page<Store> stores = storeRepository.searchStoresForAdmin(condition,pageable);

        return stores.map(StoreAdminResponse::from);
    }

    @Transactional
    public void updateStoreStatus(User user, Long storeId, StoreStatusRequest request) {

        Store store = storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("가게 없음"));

        if (request.getActive() != null) {

            boolean before = store.isActive();

            store.changeActive(request.getActive());

            boolean after = store.isActive();

            adminLogService.saveLog(user, TargetType.STORE, store.getId(), AdminAction.STORE_ACTIVE_CHANGED,
                    "가게 운영 상태 변경",
                    before ? "운영" : "중지",
                    after ? "운영" : "중지"
            );
        }

        if (request.getDeleted() != null) {

            boolean before = store.isDeleted();

            store.delete(request.getDeleted());

            boolean after = store.isDeleted();

            adminLogService.saveLog(user, TargetType.STORE, store.getId(), AdminAction.STORE_DELETED_CHANGED,
            "가게 삭제 상태 변경",
                    before ? "정상" : "삭제",
                    after ? "정상" : "삭제"
            );
        }

        boolean afterActive = store.isActive();

    }
}
