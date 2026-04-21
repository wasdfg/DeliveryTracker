package com.example.deliverytracker.cart.service;

import com.example.deliverytracker.cart.dto.CartItemRequest;
import com.example.deliverytracker.cart.dto.CartItemUpdateRequest;
import com.example.deliverytracker.cart.dto.CartResponse;
import com.example.deliverytracker.cart.dto.RedisCartItem;
import com.example.deliverytracker.cart.dto.RedisCartOption;
import com.example.deliverytracker.cart.entity.CartItem;
import com.example.deliverytracker.cart.entity.CartOption;
import com.example.deliverytracker.cart.repository.CartItemRepository;
import com.example.deliverytracker.store.entity.Product;
import com.example.deliverytracker.store.repository.OptionRepository;
import com.example.deliverytracker.store.repository.ProductRepository;
import com.example.deliverytracker.user.entitiy.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ProductRepository productRepository;

    private final CartItemRepository cartItemRepository;

    private final OptionRepository optionRepository;

    private static final String CART_KEY_PREFIX = "cart:";
    private static final long CART_TTL_DAYS = 3;

    public void addItemToCart(User user, CartItemRequest request) {
        String redisKey = CART_KEY_PREFIX + user.getId();

        String cartItemId = generateCartItemId(request.getProductId(), request.getOptionIds());

        HashOperations<String, String, RedisCartItem> hashOps = redisTemplate.opsForHash();
        RedisCartItem existingItem = hashOps.get(redisKey, cartItemId);

        if (existingItem != null) {
            RedisCartItem updatedItem = existingItem.withUpdatedQuantity(existingItem.quantity() + request.getQuantity());
            hashOps.put(redisKey, cartItemId, updatedItem);
        } else {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

            List<RedisCartOption> redisOptions = fetchAndMapOptions(request.getOptionIds());

            RedisCartItem newItem = new RedisCartItem(
                    cartItemId, product.getId(), product.getName(), product.getPrice(), request.getQuantity(), redisOptions
            );
            hashOps.put(redisKey, cartItemId, newItem);
        }

        redisTemplate.expire(redisKey, CART_TTL_DAYS, TimeUnit.DAYS);
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

        String redisKey = CART_KEY_PREFIX + user.getId();
        HashOperations<String, String, RedisCartItem> hashOps = redisTemplate.opsForHash();

        List<RedisCartItem> items = hashOps.values(redisKey);

        return new CartResponse(items);
    }

    @Transactional
    public void updateCartItemQuantity(User user, String cartItemId, CartItemUpdateRequest request){

        String redisKey = CART_KEY_PREFIX + user.getId();
        HashOperations<String, String, RedisCartItem> hashOps = redisTemplate.opsForHash();

        RedisCartItem existingItem = hashOps.get(redisKey, cartItemId);
        if (existingItem == null) {
            throw new EntityNotFoundException("해당 장바구니 아이템을 찾을 수 없습니다.");
        }

        RedisCartItem updatedItem = existingItem.withUpdatedQuantity(request.getQuantity());
        hashOps.put(redisKey, cartItemId, updatedItem);
        redisTemplate.expire(redisKey, CART_TTL_DAYS, TimeUnit.DAYS);
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

    private String generateCartItemId(Long productId, List<Long> optionIds) {
        if (optionIds == null || optionIds.isEmpty()) {
            return "prod:" + productId + ":opts:none";
        }
        String sortedOptions = optionIds.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return "prod:" + productId + ":opts:" + sortedOptions;
    }

    private List<RedisCartOption> fetchAndMapOptions(List<Long> optionIds) {
        if (optionIds == null || optionIds.isEmpty()) return List.of();

        return optionRepository.findAllByIdIn(optionIds).stream()
                .map(opt -> new RedisCartOption(opt.getId(), opt.getName(), opt.getAdditionalPrice()))
                .collect(Collectors.toList());
    }
}