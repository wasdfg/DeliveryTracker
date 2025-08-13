package com.example.deliverytracker.delivery.controller;

import com.example.deliverytracker.delivery.dto.DeliveryRequest;
import com.example.deliverytracker.delivery.dto.DeliveryResponse;
import com.example.deliverytracker.delivery.entity.DeliveryStatus;
import com.example.deliverytracker.delivery.service.DeliveryService;
import com.example.deliverytracker.rider.entity.Rider;
import com.example.deliverytracker.rider.repository.RiderRepository;
import com.example.deliverytracker.user.entitiy.User;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class DeliveryController {

    private final DeliveryService deliveryService;

    private final RiderRepository riderRepository;

    @PostMapping("/orders/{orderId}/deliveries")
    public ResponseEntity<String> createDeliveryForOrder(@PathVariable Long orderId,@RequestBody @Valid DeliveryRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        deliveryService.requestDelivery(orderId, request, userDetails.getUser());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("주문이 완료되었습니다.");
    }

    @GetMapping("/delivery/my")
    public ResponseEntity<Page<DeliveryResponse>> getMyDeliveryInfo(@AuthenticationPrincipal UserDetailsImpl userDetails, @PageableDefault(size = 10) Pageable pageable) {
        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        Page<DeliveryResponse> deliveries = deliveryService.getMyDeliveryInfo(userDetails.getUser(),pageable);
        
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/delivery/{id}")
    public ResponseEntity<DeliveryResponse> getDeliveryDetailInfo(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        DeliveryResponse deliveryDetail = deliveryService.getDeliveryDetail(id,userDetails.getUser());

        return ResponseEntity.ok(deliveryDetail);
    }

    @PatchMapping("/delivery/{id}/assign")
    public ResponseEntity<DeliveryResponse> assginDelivery(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(!userDetails.getUser().getRole().equals(User.Role.RIDER)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        User riderUser = userDetails.getUser();

        Rider rider = riderRepository.findById(riderUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("라이더를 찾을 수 없습니다."));

        DeliveryResponse response = deliveryService.assignDelivery(id, rider);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/delivery/{id}/status")
    public ResponseEntity<DeliveryResponse> changeStatus(@PathVariable Long id, @RequestParam  DeliveryStatus status, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(!userDetails.getUser().getRole().equals(User.Role.RIDER)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Rider rider = riderRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("라이더를 찾을 수 없습니다."));

        DeliveryResponse response = deliveryService.changeStatus(id,rider,status);

        return ResponseEntity.ok(response);
    }
}
