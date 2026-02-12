package com.example.deliverytracker.review.service;

import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.repository.OrderRepository;
import com.example.deliverytracker.redis.RedisPublisher;
import com.example.deliverytracker.redis.dto.NewReviewEvent;
import com.example.deliverytracker.review.dto.ReviewCreateRequest;
import com.example.deliverytracker.review.dto.ReviewResponse;
import com.example.deliverytracker.review.dto.ReviewUpdateRequest;
import com.example.deliverytracker.review.entity.Review;
import com.example.deliverytracker.review.repository.ReviewRepository;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.store.repository.StoreRepository;
import com.example.deliverytracker.store.service.BlacklistService;
import com.example.deliverytracker.user.entitiy.User;
import com.example.deliverytracker.image.service.ImageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final OrderRepository orderRepository;

    private final ReviewRepository reviewRepository;

    private final StoreRepository storeRepository;

    private final ImageService imageService;

    private final RedisPublisher redisPublisher;

    private final BlacklistService blacklistService;

    @Transactional
    public void writeReview(Long orderId, ReviewCreateRequest request, User user, MultipartFile imageFile){

        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문 정보가 없습니다."));

        blacklistService.validateNotBlacklisted(order.getStore().getId(), user.getId());

        if(!order.getUser().equals(user)){
            throw new AccessDeniedException("주문자만 리뷰를 등록 할 수 있습니다");
        }

        if (reviewRepository.existsByOrder(order)) {
            throw new IllegalArgumentException("이미 해당 주문에 대한 리뷰가 존재합니다.");
        }

        String imageUrl = null;

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                imageUrl = imageService.upload(imageFile);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드에 실패했습니다.", e);
            }
        }

        Store store = order.getStore();

        Review review = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .user(user)
                .store(order.getStore())
                .order(order)
                .imageUrl(imageUrl)
                .build();

        reviewRepository.save(review);

        store.addReview(review.getRating());

        NewReviewEvent event = new NewReviewEvent(
                review.getId(),
                order.getStore().getId(),
                user.getNickname(),
                request.getRating(),
                request.getContent()
        );

        redisPublisher.publish("order-channel", event);
    }

    public Page<ReviewResponse> getReviewList(Long storeId, Pageable pageable){

        Store store = this.storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("가게 정보가 없습니다."));

        Page<Review> page = this.reviewRepository.getReviewList(storeId,pageable);

        return page.map(ReviewResponse::from);
    }

    @Transactional
    public void updateReview(Long reviewId, ReviewUpdateRequest request,User user,MultipartFile imageFile){
        Review review = this.reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰가 없습니다."));

        if(!review.getUser().equals(user)){
            throw new AccessDeniedException("주문자만 리뷰를 변경 할 수 있습니다");
        }

        String newImageUrl = review.getImageUrl();

        if (imageFile != null && !imageFile.isEmpty()) {
            try {

                if (review.getImageUrl() != null) {
                    imageService.delete(review.getImageUrl());
                }

                newImageUrl = imageService.upload(imageFile);

            } catch (IOException e) {
                throw new RuntimeException("이미지 수정에 실패했습니다.", e);
            }
        }
        else if (request.getDeleteImage() != null && request.getDeleteImage()) {
            if (review.getImageUrl() != null) {
                imageService.delete(review.getImageUrl());
            }

            newImageUrl = null;
        }

        review.changeInfo(request,newImageUrl);
    }

    @Transactional
    public void deleteReview(Long reviewId, User user){
        Review review = this.reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰가 없습니다."));

        if(!review.getUser().equals(user)){
            throw new AccessDeniedException("주문자만 리뷰를 삭제 할 수 있습니다");
        }

        imageService.delete(review.getImageUrl());

        review.delete();
    }
}
