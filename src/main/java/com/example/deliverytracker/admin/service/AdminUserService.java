package com.example.deliverytracker.admin.service;

import com.example.deliverytracker.admin.dto.UserSearchCondition;
import com.example.deliverytracker.admin.entity.AdminAction;
import com.example.deliverytracker.admin.entity.TargetType;
import com.example.deliverytracker.user.dto.UserResponse;
import com.example.deliverytracker.user.entity.User;
import com.example.deliverytracker.user.repository.UserRepository;
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

}
