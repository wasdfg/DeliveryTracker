package com.example.deliverytracker.admin.service;

import com.example.deliverytracker.admin.dto.AdminUserResponse;
import com.example.deliverytracker.admin.dto.UserSearchCondition;
import com.example.deliverytracker.admin.entity.AdminAction;
import com.example.deliverytracker.admin.entity.TargetType;
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

import java.time.LocalDateTime;
import java.time.ZoneId;

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

    public AdminUserResponse getUserInfo(Long userId){
        User user = userRepository.getReferenceById(userId);

        return new AdminUserResponse(user);
    }

    @Transactional
    public void updateUserStatus(User admin, Long userId, User.Status status){

        User.Status beforeStatus = admin.getStatus();

        switch (status) {

            case WITHDRAWN -> admin.withdraw();

            case ACTIVE -> admin.restore();

            default -> admin.changeStatus(status);
        }

        User.Status afterStatus = admin.getStatus();

        adminLogService.saveLog(admin, TargetType.USER, userId, AdminAction.USER_STATUS_CHANGED, "회원 상태 변경", beforeStatus.name(), afterStatus.name());
    }


    @Transactional
    public void suspendUser(User admin, Long userId, Integer suspensionDays) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        User.Status beforeStatus = user.getStatus();

        LocalDateTime suspendedUntil = null;

        if (suspensionDays != null) {
            suspendedUntil = LocalDateTime.now(ZoneId.of("Asia/Seoul")).plusDays(suspensionDays);
        }

        user.suspend(suspendedUntil);

        User.Status afterStatus = user.getStatus();

        adminLogService.saveLog(admin, TargetType.USER, userId, AdminAction.USER_STATUS_CHANGED, suspensionDays == null ? "회원 영구 정지" : "회원 " + suspensionDays + "일 정지", beforeStatus.name(), afterStatus.name());
    }
}
