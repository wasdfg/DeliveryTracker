package com.example.deliverytracker.store.listener;

import com.example.deliverytracker.store.entity.ReviewCreatedEvent;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.store.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class StoreRatingEventListener {
    private final StoreRepository storeRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReviewCreated(ReviewCreatedEvent event) {
        Store store = storeRepository.findById(event.storeId())
                .orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

        store.addRating(event.rating());
        storeRepository.save(store);
    }
}