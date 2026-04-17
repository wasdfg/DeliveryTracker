package com.example.deliverytracker.order.service;

import com.example.deliverytracker.coupon.service.CouponService;
import com.example.deliverytracker.notification.service.NotificationService;
import com.example.deliverytracker.order.dto.OrderCreateRequest;
import com.example.deliverytracker.order.dto.OrderHistoryDto;
import com.example.deliverytracker.order.dto.OrderResponse;
import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.entity.OrderHistory;
import com.example.deliverytracker.order.entity.OrderOption;
import com.example.deliverytracker.order.repository.OrderHistoryRepository;
import com.example.deliverytracker.redis.dto.OrderCreatedEvent;
import com.example.deliverytracker.order.entity.OrderItem;
import com.example.deliverytracker.order.repository.OrderRepository;
import com.example.deliverytracker.redis.RedisPublisher;
import com.example.deliverytracker.store.entity.Option;
import com.example.deliverytracker.store.entity.Product;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.store.repository.OptionRepository;
import com.example.deliverytracker.store.repository.ProductRepository;
import com.example.deliverytracker.store.repository.StoreRepository;
import com.example.deliverytracker.store.service.BlacklistService;
import com.example.deliverytracker.store.service.ProductService;
import com.example.deliverytracker.user.entitiy.User;
import com.example.deliverytracker.util.geocoding.Coordinates;
import com.example.deliverytracker.util.geocoding.GeocodingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final StoreRepository storeRepository;

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final RedisPublisher redisPublisher;

    private final GeocodingService geocodingService;

    private final CouponService couponService;

    private final ProductService productService;

    private final BlacklistService blacklistService;

    private final OptionRepository optionRepository;

    private final NotificationService notificationService;

    private final OrderHistoryRepository orderHistoryRepository;

    @Transactional
    public void createOrder(OrderCreateRequest request, User user, Long userCouponId){
        if(!user.getRole().equals(User.Role.USER)){
            throw new EntityNotFoundException("일반 사용자만 주문할 수 있습니다.");
        }

        Store store = storeRepository.findByIdAndActiveTrue(request.getStoreId())
                .orElseThrow(() -> new EntityNotFoundException("가게 정보가 없습니다."));

        blacklistService.validateNotBlacklisted(request.getStoreId(), user.getId());

        if (!store.isCurrentlyOrderable()) {
            throw new IllegalStateException("현재 주문 가능한 시간이 아닙니다.");
        }

        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal totalPrice = BigDecimal.valueOf(0);

        int discountAmount = 0;



        for (OrderCreateRequest.Item item : request.getItems()) {

            productService.validateProductAvailability(item.getProductId());

            Product product = productRepository.findByIdAndStoreId(item.getProductId(), store.getId())
                    .orElseThrow(() -> new EntityNotFoundException("상품 정보를 찾을 수 없습니다."));

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            totalPrice = totalPrice.add(itemTotal);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .name(product.getName())
                    .price(product.getPrice())
                    .quantity(item.getQuantity())
                    .imageUrl(product.getImageUrl())
                    .build();

            BigDecimal itemOptionsTotalPrice = BigDecimal.ZERO;

            if (item.getOptionIds() != null && !item.getOptionIds().isEmpty()) {
                List<Option> selectedOptions = optionRepository.findAllByIdIn(item.getOptionIds());

                for (Option opt : selectedOptions) {
                    OrderOption orderOption = OrderOption.builder()
                            .name(opt.getName())
                            .price(opt.getAdditionalPrice())
                            .orderItem(orderItem)
                            .build();


                    orderItem.getOrderOptions().add(orderOption);

                    itemOptionsTotalPrice = itemOptionsTotalPrice.add(opt.getAdditionalPrice());
                }
            }

            BigDecimal itemFinalPrice = product.getPrice().add(itemOptionsTotalPrice)
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            orderItems.add(orderItem);
        }

        if (userCouponId != null) {
            discountAmount = couponService.useCoupon(user.getId(), userCouponId);
        }

        BigDecimal finalPrice = totalPrice.subtract(BigDecimal.valueOf(discountAmount));

        Coordinates coords = geocodingService.getCoordinates(request.getDeliveryAddress());

        if (store.getCurrentDeliveryTime() == null) {
            throw new IllegalStateException("가게에서 배달 시간을 설정하지 않아 주문할 수 없습니다.");
        }

        Order order = Order.builder()
                .user(user)
                .store(store)
                .deliveryAddress(request.getDeliveryAddress())
                .pickupAddress(request.getPickupAddress())
                .deliveryLatitude(coords.getLatitude())   // 변환된 위도 저장
                .deliveryLongitude(coords.getLongitude()) // 변환된 경도 저장
                .requestedAt(LocalDateTime.now())
                .estimatedDeliveryTime(store.getCurrentDeliveryTime().getDescription())
                .status(Order.Status.PENDING)
                .totalPrice(finalPrice)
                .orderItems(orderItems)
                .build();

        OrderHistory history = OrderHistory.builder()
                .order(order)
                .previousStatus(Order.Status.PENDING)
                .newStatus(Order.Status.ACCEPTED)
                .changedBy("OWNER")
                .reason(null)
                .build();

        for (OrderItem item : orderItems) {
            item.assignOrder(order);
        }

        orderRepository.save(order);

        orderHistoryRepository.save(history);

        redisPublisher.publish("order-channel", new OrderCreatedEvent(order.getId(), store.getId()));

        notificationService.sendNewOrderNotification(order.getStore().getId(), user.getId(),order.getId());

    }

    @Transactional(readOnly = true)
    public OrderResponse findOrderInfo(Long id, User user){

        Order order = orderRepository.findOrderWithDetailsById(id)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        if (!order.getUser().getId().equals(user.getId())) {

            throw new EntityNotFoundException("주문을 조회할 권한이 없습니다.");
        }

        return OrderResponse.from(order);

    }

    public Page<OrderHistoryDto> findMyOrderHistory(String statusStr, Pageable pageable, User user){

        Page<Order> orderPage;

        if (statusStr != null && !statusStr.isBlank()) {
            try {
                Order.Status status = Order.Status.valueOf(statusStr.toUpperCase());
                orderPage = orderRepository.findAllByUserAndStatusOrderByRequestedAtDesc(user, status, pageable);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 주문 상태입니다: " + statusStr);
            }
        } else {

            orderPage = orderRepository.findAllByUserOrderByRequestedAtDesc(user, pageable);
        }

        return orderPage.map(OrderHistoryDto::new);

    }

    @Transactional
    public void updateOrderStatus(Long orderId, String statusStr, User user){

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        if (!order.getStore().getOwner().getId().equals(user.getId())) {
            throw new EntityNotFoundException("주문 상태를 변경할 권한이 없습니다.");
        }

        Order.Status newStatus;

        try {
            newStatus = Order.Status.valueOf(statusStr.toUpperCase());
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 주문 상태입니다: " + statusStr);
        }

        order.updateStatus(newStatus);

        order.changeStatus(newStatus);

        notificationService.notifyOrderStatusChanged(
                order.getId(),
                order.getUser().getId(),
                newStatus
        );
    }

    @Transactional
    public void cancelOrder(Long orderId, User user,String reason){

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("주문 상태를 변경할 권한이 없습니다.");
        }

        Order.Status currentStatus = order.getStatus();
        if (currentStatus == Order.Status.COMPLETED || currentStatus == Order.Status.CANCELED) {
            throw new IllegalStateException("이미 완료되었거나 취소된 주문은 취소할 수 없습니다.");
        }

        order.cancel(Order.Status.CANCELED);

        OrderHistory history = OrderHistory.builder()
                .order(order)
                .previousStatus(order.getStatus())
                .newStatus(Order.Status.CANCELED)
                .changedBy("OWNER")
                .reason(reason)
                .build();

        orderHistoryRepository.save(history);
    }

    @Transactional
    public void deleteOrder(Long orderId, User user){

        if (user.getRole() != User.Role.ADMIN) {
            throw new AccessDeniedException("주문을 삭제할 권한이 없습니다.");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        order.delete(true);

    }

    public Page<OrderHistoryDto> findStoreOrders(Long storeId, Pageable pageable, User user) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("본인 가게의 주문만 조회할 수 있습니다.");
        }

        Page<Order> storeOrders = orderRepository.findAllByStoreIdOrderByRequestedAtDesc(storeId, pageable);

        return storeOrders.map(OrderHistoryDto::new);
    }

    public void autoCancelUnacceptedOrders() {

        LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(10);

        List<Order> unacceptedOrders = orderRepository.findByStatusAndCreatedAtBefore(
                Order.Status.PENDING, thresholdTime
        );

        if (unacceptedOrders.isEmpty()) return;

        for (Order order : unacceptedOrders) {
            order.cancelBySystem("시스템 자동 취소: 가게 미접수 (10분 경과)");
        }

        log.info("가게 미접수 자동 취소 처리 완료: {}건", unacceptedOrders.size());
    }

    @Transactional
    public void autoCompleteDeliveringOrders() {
        LocalDateTime thresholdTime = LocalDateTime.now().minusHours(2);

        List<Order> deliveringOrders = orderRepository.findByStatusAndCreatedAtBefore(
                Order.Status.DELIVERING, thresholdTime
        );

        if (deliveringOrders.isEmpty()) return;

        for (Order order : deliveringOrders) {
            order.completeBySystem();
        }

        log.info("배달 중 장기 체류 주문 자동 완료 처리: {}건", deliveringOrders.size());
    }
}
