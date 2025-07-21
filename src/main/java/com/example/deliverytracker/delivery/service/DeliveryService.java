package com.example.deliverytracker.delivery.service;

import com.example.deliverytracker.delivery.dto.DeliveryRequestDto;
import com.example.deliverytracker.delivery.dto.DeliveryResponseDto;
import com.example.deliverytracker.delivery.entity.Delivery;
import com.example.deliverytracker.delivery.entity.DeliveryStatus;
import com.example.deliverytracker.delivery.repository.DeliveryRepository;
import com.example.deliverytracker.rider.entity.Rider;
import com.example.deliverytracker.user.entitiy.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.EnumSet;
import java.util.Optional;

@Slf4j //로그용
@RequiredArgsConstructor
@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public void requestDelivery(DeliveryRequestDto requestDto, User user) {
        Delivery delivery = Delivery.builder()
                .receiverName(requestDto.getReceiverName())
                .receiverAddress(requestDto.getReceiverAddress())
                .receiverPhone(requestDto.getReceiverPhone())
                .itemDescription(requestDto.getItemDescription())
                .status(DeliveryStatus.REQUESTED)
                .requester(user)
                .build();

        deliveryRepository.save(delivery);
        log.info("New delivery request created: {}", delivery.getId());
    }

    public Page<DeliveryResponseDto> getMyDeliveryInfo(User user, Pageable pageable){

        if (user.getRole().equals(User.Role.ADMIN)) {
            return Page.empty();
        }

        Page<Delivery> deliveries = this.deliveryRepository.findByRequesterId(user.getId(),pageable); //리포지토리는 delivery 엔티티를 사용하는데 dto로 리턴하려면 무슨 방식을 사용해야할지?

        return deliveries.map(DeliveryResponseDto::from);
    }

    public DeliveryResponseDto getDeliveryDetail(Long id,User user){

        Optional<Delivery> optionalDelivery = deliveryRepository.findById(id);

        if (optionalDelivery.isEmpty()) {
            throw new EntityNotFoundException("해당 배송 정보를 찾을 수 없습니다.");
        }

        Delivery delivery = optionalDelivery.get();

        return DeliveryResponseDto.from(delivery);
    }

    @Transactional
    public DeliveryResponseDto assignDelivery(Long deliveryId, Rider rider){

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("배송을 찾을 수 없습니다."));

        delivery.assignRider(rider);

        return DeliveryResponseDto.from(delivery);
    }

    @Transactional
    public DeliveryResponseDto changeStatus(Long deliveryId, Rider riderUser,DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("배송을 찾을 수 없습니다."));

        if (!delivery.getRider().getId().equals(riderUser.getId())) {
            throw new AccessDeniedException("해당 배송은 로그인한 라이더의 것이 아닙니다.");
        }

        if (!EnumSet.of(DeliveryStatus.IN_PROGRESS, DeliveryStatus.DELIVERED, DeliveryStatus.CANCELLED)
                .contains(status)) {
            throw new IllegalArgumentException("해당 상태로는 변경할 수 없습니다: " + status);
        }

        delivery.updateStatus(status);

        return DeliveryResponseDto.from(delivery);
    }
}
