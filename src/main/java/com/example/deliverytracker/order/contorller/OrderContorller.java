package com.example.deliverytracker.order.contorller;


import com.example.deliverytracker.order.dto.OrderCreateRequest;
import com.example.deliverytracker.order.dto.OrderStatusUpdateRequest;
import com.example.deliverytracker.order.service.OrderService;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/order")
@RestController
public class OrderContorller {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderCreateRequest request,@AuthenticationPrincipal UserDetailsImpl userDetails) {

        orderService.createOrder(request,userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body("주문이 완료되었습니다.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOrderInfo(@PathVariable Long id,@AuthenticationPrincipal UserDetailsImpl userDetails){

        OrderResponse response = orderService.findOrderInfo(id,userDetails.getUser());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> findAllOrders(@RequestParam(required = false) String status, Pageable pageable,@AuthenticationPrincipal UserDetailsImpl userDetails) {

        orderService.findAllOrders(status, pageable,userDetails.getUser());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatusUpdateRequest request,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderService.updateOrderStatus(id, request.getStatus(),userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id,@AuthenticationPrincipal UserDetailsImpl userDetails) {

        orderService.cancelOrder(id,userDetails.getUser());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        orderService.deleteOrder(id, userDetails.getUser());

        return ResponseEntity.noContent().build();
    }
}
