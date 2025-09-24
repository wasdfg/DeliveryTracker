package com.example.deliverytracker.delivery.service;

import com.example.deliverytracker.delivery.entity.LocationMessage;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.repository.OrderRepository;
import com.example.deliverytracker.redis.RedisPublisher;
import com.example.deliverytracker.redis.dto.RiderArrivingEvent;
import com.example.deliverytracker.util.LocationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ProximityService {

    private static final double ARRIVAL_THRESHOLD_METERS = 500; // 500미터 이내로 들어오면 알림

    private final OrderRepository orderRepository;
    private final RedisPublisher redisPublisher;
    private final RedisTemplate<String, String> redisTemplate;

    public void checkProximity(LocationMessage message) {

        String redisKey = "arrival-notified:" + message.getOrderId();
        Boolean alreadyNotified = redisTemplate.opsForValue().get(redisKey) != null;

        if (alreadyNotified) {
            return;
        }

        // 주문 정보를 DB에서 조회하여 배달지 좌표를 가져옴
        Order order = orderRepository.findById(message.getOrderId())
                .orElse(null); // 실제로는 예외 처리 필요

        if (order == null || order.getDeliveryLatitude() == null) {
            return;
        }

        // 라이더와 배달지 사이의 거리 계산
        double distance = LocationUtil.calculateDistanceInMeters(
                message.getLatitude(),
                message.getLongitude(),
                order.getDeliveryLatitude(),
                order.getDeliveryLongitude()
        );


        if (distance <= ARRIVAL_THRESHOLD_METERS) {

            RiderArrivingEvent event = new RiderArrivingEvent(order.getId(), order.getUser().getId());
            redisPublisher.publish("order-channel", event);

            // 알림을 보냈다고 Redis에 기록 (중복 발송 방지, 1시간 유효)
            redisTemplate.opsForValue().set(redisKey, "true", 1, TimeUnit.HOURS);
        }
    }
}
