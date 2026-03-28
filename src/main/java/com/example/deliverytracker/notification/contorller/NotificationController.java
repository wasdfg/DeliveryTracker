package com.example.deliverytracker.notification.contorller;

import com.example.deliverytracker.notification.dto.NotificationResponse;
import com.example.deliverytracker.notification.entity.Notification;
import com.example.deliverytracker.notification.service.NotificationService;
import com.example.deliverytracker.user.entitiy.User;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(@AuthenticationPrincipal UserDetailsImpl user) {
        List<NotificationResponse> notifications = notificationService.getMyNotifications(user.getUser().getId());
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> readNotification(@PathVariable Long id) {
        notificationService.readNotification(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(@AuthenticationPrincipal UserDetailsImpl user) {
        Long userId = user.getUser().getId();
        Long count = notificationService.getUnreadNotificationCount(userId);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Void> readAllNotifications(@AuthenticationPrincipal UserDetailsImpl user) {
        notificationService.readAllNotifications(user.getUser().getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl user) {
        notificationService.deleteNotification(id, user.getUser().getId());
        return ResponseEntity.noContent().build();
    }
}
