package com.example.deliverytracker.notification.repository;

import com.example.deliverytracker.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findTop20ByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    Long countByReceiverIdAndIsReadFalse(Long receiverId);

}
