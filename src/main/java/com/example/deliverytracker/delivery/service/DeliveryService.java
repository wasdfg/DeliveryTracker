package com.example.deliverytracker.delivery.service;

import com.example.deliverytracker.delivery.dto.DeliveryRequest;
import com.example.deliverytracker.delivery.dto.DeliveryResponse;
import com.example.deliverytracker.delivery.entity.Delivery;
import com.example.deliverytracker.delivery.entity.DeliveryStatus;
import com.example.deliverytracker.delivery.repository.DeliveryRepository;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.repository.OrderRepository;
import com.example.deliverytracker.order.service.OrderService;
import com.example.deliverytracker.redis.RedisPublisher;
import com.example.deliverytracker.redis.dto.DeliveryStartedEvent;
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

    private final OrderRepository orderRepository;

    private final OrderService orderService;

    private final RedisPublisher redisPublisher;

    @Transactional
    public void requestDelivery(Long orderId,DeliveryRequest requestDto, User user) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("배달을 요청할 주문이 없습니다."));

        Delivery delivery = Delivery.builder()
                .order(order)
                .receiverName(requestDto.getReceiverName())
                .receiverAddress(requestDto.getReceiverAddress())
                .receiverPhone(requestDto.getReceiverPhone())
                .itemDescription(requestDto.getItemDescription())
                .status(DeliveryStatus.WAITING)
                .requester(user)
                .build();

        deliveryRepository.save(delivery);
        log.info("New delivery request created for orderId {}: deliveryId {}", orderId, delivery.getId());

        orderService.updateOrderStatus(orderId, "WAITING_DELIVERY", user);
    }

    public Page<DeliveryResponse> getMyDeliveryInfo(User user, Pageable pageable){

        if (user.getRole().equals(User.Role.ADMIN)) {
            return Page.empty();
        }

        Page<Delivery> deliveries = this.deliveryRepository.findByRequesterId(user.getId(),pageable); //리포지토리는 delivery 엔티티를 사용하는데 dto로 리턴하려면 무슨 방식을 사용해야할지?

        return deliveries.map(DeliveryResponse::from);
    }

    public DeliveryResponse getDeliveryDetail(Long id, User user){

        Optional<Delivery> optionalDelivery = deliveryRepository.findById(id);

        if (optionalDelivery.isEmpty()) {
            throw new EntityNotFoundException("해당 배송 정보를 찾을 수 없습니다.");
        }

        Delivery delivery = optionalDelivery.get();

        return DeliveryResponse.from(delivery);
    }

    @Transactional
    public DeliveryResponse assignDelivery(Long deliveryId, Rider rider){

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("배송을 찾을 수 없습니다."));

        delivery.assignRider(rider);

        return DeliveryResponse.from(delivery);
    }

    @Transactional
    public DeliveryResponse changeStatus(Long deliveryId, Rider riderUser, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("배송을 찾을 수 없습니다."));

        if (!delivery.getRider().getId().equals(riderUser.getId())) {
            throw new AccessDeniedException("해당 배송은 로그인한 라이더의 것이 아닙니다.");
        }

        if (!EnumSet.of(DeliveryStatus.PICKED_UP, DeliveryStatus.DELIVERING ,DeliveryStatus.DELIVERED, DeliveryStatus.FAILED)
                .contains(status)) {
            throw new IllegalArgumentException("해당 상태로는 변경할 수 없습니다: " + status);
        }

        delivery.updateStatus(status);

        if(status.equals(DeliveryStatus.PICKED_UP)){
            Order order = delivery.getOrder();

            DeliveryStartedEvent event = new DeliveryStartedEvent(order.getId(), order.getUser().getId(), riderUser.getUser().getNickname());
            redisPublisher.publish("order-channel", event);
        }

        if(status.equals(DeliveryStatus.DELIVERED)){
            delivery.getOrder().changeStatus(Order.Status.COMPLETED);
        }

        return DeliveryResponse.from(delivery);
    }
}
