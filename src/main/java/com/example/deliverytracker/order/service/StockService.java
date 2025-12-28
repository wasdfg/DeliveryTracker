package com.example.deliverytracker.order.service;

import com.example.deliverytracker.store.entity.Product;
import com.example.deliverytracker.store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final RedissonClient redissonClient;
    private final ProductRepository productRepository;

    public void decreaseProductStock(Long productId, int quantity) {

        RLock lock = redissonClient.getLock("LOCK:PRODUCT:" + productId);

        try {
            boolean available = lock.tryLock(5, 2, TimeUnit.SECONDS);

            if (!available) {
                throw new RuntimeException("현재 주문량이 많아 처리가 지연되고 있습니다. 다시 시도해주세요.");
            }

            updateStock(productId, quantity);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Transactional
    public void updateStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

        product.decreaseStock(quantity);
    }
}