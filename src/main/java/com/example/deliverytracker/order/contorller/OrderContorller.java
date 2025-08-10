package com.example.deliverytracker.order.contorller;


import com.example.deliverytracker.order.dto.OrderCreateRequest;
import com.example.deliverytracker.order.dto.OrderResponse;
import com.example.deliverytracker.order.dto.OrderStatusUpdateRequest;
import com.example.deliverytracker.order.service.OrderService;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/orders")
@RestController
public class OrderContorller {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderCreateRequest request,@AuthenticationPrincipal UserDetailsImpl userDetails) {

        orderService.createOrder(request,userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body("주문이 완료되었습니다.");
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> findOrderInfo(@PathVariable Long orderId,@AuthenticationPrincipal UserDetailsImpl userDetails){

        OrderResponse response = orderService.findOrderInfo(orderId,userDetails.getUser());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> findAllOrders(@RequestParam(required = false) String status, Pageable pageable,@AuthenticationPrincipal UserDetailsImpl userDetails) {

        Page<OrderResponse> response = orderService.findAllOrders(status, pageable,userDetails.getUser());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusUpdateRequest request,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderService.updateOrderStatus(orderId, request.getStatus(),userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId,@AuthenticationPrincipal UserDetailsImpl userDetails) {

        orderService.cancelOrder(orderId,userDetails.getUser());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        orderService.deleteOrder(orderId, userDetails.getUser());

        return ResponseEntity.noContent().build();
    }
}
