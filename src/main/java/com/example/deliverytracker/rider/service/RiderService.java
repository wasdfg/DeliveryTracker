package com.example.deliverytracker.rider.service;

import com.example.deliverytracker.rider.dto.RiderProfileResponse;
import com.example.deliverytracker.rider.dto.RiderStatusRequest;
import com.example.deliverytracker.rider.entity.Rider;
import com.example.deliverytracker.rider.repository.RiderRepository;
import com.example.deliverytracker.user.entitiy.User;
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
}
