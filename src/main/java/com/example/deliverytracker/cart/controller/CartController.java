package com.example.deliverytracker.cart.controller;

import com.example.deliverytracker.cart.dto.CartItemRequestDto;
import com.example.deliverytracker.cart.dto.CartResponseDto;
import com.example.deliverytracker.cart.service.CartService;
import com.example.deliverytracker.user.entitiy.User;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<String> addItemToCart(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody CartItemRequestDto requestDto) {

        User user = userDetails.getUser();
        
        cartService.addItemToCart(user, requestDto);

        return ResponseEntity.ok("장바구니에 상품을 담았습니다.");
    }

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        CartResponseDto cartResponseDto = cartService.getCart(user);

        return ResponseEntity.ok(cartResponseDto);
    }
}