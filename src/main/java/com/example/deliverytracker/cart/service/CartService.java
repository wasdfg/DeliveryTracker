package com.example.deliverytracker.cart.service;

import com.example.deliverytracker.cart.dto.CartItemRequestDto;
import com.example.deliverytracker.cart.dto.CartResponseDto;
import com.example.deliverytracker.cart.entity.Cart;
import com.example.deliverytracker.cart.entity.CartItem;
import com.example.deliverytracker.cart.repository.CartItemRepository;
import com.example.deliverytracker.cart.repository.CartRepository;
import com.example.deliverytracker.store.entity.Product;
import com.example.deliverytracker.store.repository.ProductRepository;
import com.example.deliverytracker.user.entitiy.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public void addItemToCart(User user, CartItemRequestDto requestDto) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> new Cart(user));

        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.updateQuantity(requestDto.getQuantity());
        }
        else {
            CartItem newCartItem = new CartItem(cart, product, requestDto.getQuantity());
            cart.addCartItem(newCartItem);
        }

        cartRepository.save(cart);
    }

    public CartResponseDto getCart(User user) {

        Optional<Cart> optionalCart = cartRepository.findByUserId(user.getId());

        if (optionalCart.isPresent()) {

            Cart cart = optionalCart.get();
            return new CartResponseDto(cart);
        } else {

            return new CartResponseDto();
        }
    }

}