package com.example.deliverytracker.cart.service;

import com.example.deliverytracker.cart.dto.CartItemRequest;
import com.example.deliverytracker.cart.dto.CartItemUpdateRequest;
import com.example.deliverytracker.cart.dto.CartResponse;
import com.example.deliverytracker.cart.entity.Cart;
import com.example.deliverytracker.cart.entity.CartItem;
import com.example.deliverytracker.cart.entity.CartOption;
import com.example.deliverytracker.cart.repository.CartItemRepository;
import com.example.deliverytracker.cart.repository.CartRepository;
import com.example.deliverytracker.store.entity.Option;
import com.example.deliverytracker.store.entity.Product;
import com.example.deliverytracker.store.repository.OptionRepository;
import com.example.deliverytracker.store.repository.ProductRepository;
import com.example.deliverytracker.user.entitiy.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final CartItemRepository cartItemRepository;

    private final OptionRepository optionRepository;

    @Transactional
    public void addItemToCart(User user, CartItemRequest request) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> new Cart(user));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .filter(item -> isSameOptionCombination(item.getCart().getCartOptions(), request.getOptionIds()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.updateQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            CartItem newCartItem = new CartItem(cart, product, request.getQuantity());

            if (request.getOptionIds() != null && !request.getOptionIds().isEmpty()) {
                List<Option> options = optionRepository.findAllByIdIn(request.getOptionIds());
                for (Option opt : options) {
                    CartOption cartOption = CartOption.builder()
                            .cartItem(newCartItem)
                            .optionId(opt.getId())
                            .name(opt.getName())
                            .price(opt.getAdditionalPrice())
                            .build();
                    newCartItem.getCart().getCartOptions().add(cartOption);
                }
            }
            cart.addCartItem(newCartItem);
        }

        cartRepository.save(cart);
    }

    private boolean isSameOptionCombination(List<CartOption> currentOptions, List<Long> requestOptionIds) {
        List<Long> requestIds = (requestOptionIds == null) ? new ArrayList<>() : requestOptionIds;

        if (currentOptions.size() != requestIds.size()) return false;

        List<Long> currentIds = currentOptions.stream()
                .map(CartOption::getOptionId)
                .sorted()
                .toList();

        List<Long> sortedRequestIds = requestIds.stream().sorted().toList();

        return currentIds.equals(sortedRequestIds);
    }

    public CartResponse getCart(User user) {

        Optional<Cart> optionalCart = cartRepository.findByUserId(user.getId());

        if (optionalCart.isPresent()) {

            Cart cart = optionalCart.get();
            return new CartResponse(cart);
        } else {

            return new CartResponse();
        }
    }

    @Transactional
    public void updateCartItemQuantity(User user, Long cartItemId, CartItemUpdateRequest request){

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("해당 장바구니 아이템을 찾을 수 없습니다."));

        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("해당 아이템에 대한 권한이 없습니다.");
        }

        cartItem.updateQuantity(request.getQuantity());
    }

    @Transactional
    public void removeCartItem(User user, Long cartItemId){

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("해당 장바구니 아이템을 찾을 수 없습니다."));

        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("해당 아이템에 대한 권한이 없습니다.");
        }

        cartItemRepository.delete(cartItem);
    }
}