package com.example.deliverytracker.cart.controller;

import com.example.deliverytracker.cart.dto.CartItemRequest;
import com.example.deliverytracker.cart.dto.CartItemUpdateRequest;
import com.example.deliverytracker.cart.dto.CartResponse;
import com.example.deliverytracker.cart.service.CartService;
import com.example.deliverytracker.user.entitiy.User;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<String> addItemToCart(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody CartItemRequest requestDto) {

        User user = userDetails.getUser();

        cartService.addItemToCart(user, requestDto);

        return ResponseEntity.ok("장바구니에 상품을 담았습니다.");
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        CartResponse cartResponseDto = cartService.getCart(user);

        return ResponseEntity.ok(cartResponseDto);
    }

    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<String> updateCartItemQuantity(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long cartItemId, @Valid @RequestBody CartItemUpdateRequest requestDto){

        User user = userDetails.getUser();

        cartService.updateCartItemQuantity(user, cartItemId , requestDto);

        return ResponseEntity.ok("장바구니에 상품을 변경하였습니다.");
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<String> removeCartItem(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long cartItemId){

        User user = userDetails.getUser();

        cartService.removeCartItem(user, cartItemId);

        return ResponseEntity.ok("장바구니에 상품을 삭제하였습니다.");
    }
}