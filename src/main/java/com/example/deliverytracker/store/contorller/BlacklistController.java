package com.example.deliverytracker.store.contorller;

import com.example.deliverytracker.store.dto.BlacklistRequest;
import com.example.deliverytracker.store.dto.BlacklistResponse;
import com.example.deliverytracker.store.service.BlacklistService;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stores/{storeId}/blacklist")
@RequiredArgsConstructor
public class BlacklistController {

    private final BlacklistService blacklistService;

    @PostMapping
    public ResponseEntity<String> blockUser(@PathVariable Long storeId, @RequestBody BlacklistRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        blacklistService.addBlacklist(storeId, request.getUserId(), request.getReason(), userDetails.getUser());
        return ResponseEntity.ok("사용자가 차단되었습니다.");
    }

    @GetMapping
    public ResponseEntity<List<BlacklistResponse>> getBlacklist(@PathVariable Long storeId) {
        return ResponseEntity.ok(blacklistService.getBlacklist(storeId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> unblockUser(@PathVariable Long storeId, @PathVariable Long userId) {
        blacklistService.unblock(storeId, userId);
        return ResponseEntity.ok("차단이 해제되었습니다.");
    }
}
