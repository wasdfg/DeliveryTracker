package com.example.deliverytracker.rider.service;

import com.example.deliverytracker.delivery.entity.Delivery;
import com.example.deliverytracker.delivery.entity.DeliveryStatus;
import com.example.deliverytracker.delivery.repository.DeliveryRepository;
import com.example.deliverytracker.delivery.service.ProximityService;
import com.example.deliverytracker.rider.dto.RiderLocationUpdateRequest;
import com.example.deliverytracker.rider.dto.RiderProfileResponse;
import com.example.deliverytracker.rider.dto.RiderStatusRequest;
import com.example.deliverytracker.rider.entity.Rider;
import com.example.deliverytracker.rider.repository.RiderRepository;
import com.example.deliverytracker.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j //로그용
@RequiredArgsConstructor
@Service
public class RiderService {

    private final RiderRepository riderRepository;

    private final DeliveryRepository deliveryRepository;

    private final ProximityService proximityService;

    public RiderProfileResponse getMyInfo(User user){
        if(!user.getRole().equals(User.Role.RIDER)){
            throw new AccessDeniedException("라이더 권한이 아닙니다.");
        }
        Rider rider = this.riderRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("라이더 정보를 찾을 수 없습니다."));

        return RiderProfileResponse.from(rider);
    }

    @Transactional
    public void changeStatus(User user, RiderStatusRequest staus){
        if(!user.getRole().equals(User.Role.RIDER)){
            throw new AccessDeniedException("라이더 권한이 아닙니다.");
        }

        Rider rider = this.riderRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("라이더 정보를 찾을 수 없습니다."));

        rider.changeStatus(staus.getStatus());
    }


    @Transactional
    public void updateLocation(User user, RiderLocationUpdateRequest request) {

        Rider rider = this.riderRepository.findByRiderId(user.getId());

        Delivery delivery = deliveryRepository.findByOrder_IdAndRider_Id(request.getOrderId(), rider.getId()).orElseThrow(() ->
                        new EntityNotFoundException("현재 라이더에게 배정된 배송이 아닙니다."));

        if (delivery.getStatus() != DeliveryStatus.PICKED_UP && delivery.getStatus() != DeliveryStatus.DELIVERING) {

            throw new IllegalStateException("현재 위치를 전송할 수 있는 배송 상태가 아닙니다.");
        }

        rider.updateLocation(request.getLatitude(), request.getLongitude());

        proximityService.checkProximity(request.getOrderId(), request.getLatitude(), request.getLongitude());
    }
}
