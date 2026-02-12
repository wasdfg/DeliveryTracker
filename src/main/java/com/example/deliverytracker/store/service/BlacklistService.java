package com.example.deliverytracker.store.service;

import com.example.deliverytracker.store.dto.BlacklistResponse;
import com.example.deliverytracker.store.entity.Blacklist;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.store.repository.BlacklistRepository;
import com.example.deliverytracker.store.repository.StoreRepository;
import com.example.deliverytracker.user.entitiy.User;
import com.example.deliverytracker.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlacklistService {

    private final BlacklistRepository blacklistRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addBlacklist(Long storeId, Long userId, String reason, User owner) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException("본인 매장의 블랙리스트만 관리할 수 있습니다.");
        }

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        if (blacklistRepository.existsByStoreIdAndUserId(storeId, userId)) {
            throw new IllegalStateException("이미 차단된 사용자입니다.");
        }

        Blacklist blacklist = Blacklist.createBlacklist(store, targetUser, reason);

        blacklistRepository.save(blacklist);
    }

    public void validateNotBlacklisted(Long storeId, Long userId) {
        if (blacklistRepository.existsByStoreIdAndUserId(storeId, userId)) {
            throw new AccessDeniedException("해당 매장의 이용이 제한된 사용자입니다.");
        }
    }

    public List<BlacklistResponse> getBlacklist(Long storeId) {
        return blacklistRepository.findAllByStoreIdOrderByCreatedAtDesc(storeId).stream()
                .map(BlacklistResponse::new)
                .toList();
    }

    @Transactional
    public void unblock(Long storeId, Long userId) {
        blacklistRepository.deleteByStoreIdAndUserId(storeId, userId);
    }
}
