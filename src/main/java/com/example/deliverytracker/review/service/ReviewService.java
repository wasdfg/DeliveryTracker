package com.example.deliverytracker.review.service;

import com.example.deliverytracker.order.entity.Order;
import com.example.deliverytracker.order.repository.OrderRepository;
import com.example.deliverytracker.review.dto.ReviewCreateRequest;
import com.example.deliverytracker.review.dto.ReviewResponse;
import com.example.deliverytracker.review.dto.ReviewUpdateRequest;
import com.example.deliverytracker.review.entity.Review;
import com.example.deliverytracker.review.repository.ReviewRepository;
import com.example.deliverytracker.store.entity.Store;
import com.example.deliverytracker.store.repository.StoreRepository;
import com.example.deliverytracker.user.entitiy.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final OrderRepository orderRepository;

    private final ReviewRepository reviewRepository;

    private final StoreRepository storeRepository;

    @Transactional
    public void writeReview(Long orderId, ReviewCreateRequest request, User user){

        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문 정보가 없습니다."));

        if(!order.getUser().equals(user)){
            throw new AccessDeniedException("주문자만 리뷰를 등록 할 수 있습니다");
        }

        if (reviewRepository.existsByOrder(order)) {
            throw new IllegalArgumentException("이미 해당 주문에 대한 리뷰가 존재합니다.");
        }

        Review review = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .user(user)
                .store(order.getStore())
                .order(order)
                .build();

        reviewRepository.save(review);
    }

    public Page<ReviewResponse> getReviewList(Long storeId, Pageable pageable){

        Store store = this.storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("가게 정보가 없습니다."));

        Page<Review> page = this.reviewRepository.getReviewList(storeId,pageable);

        return page.map(ReviewResponse::from);
    }

    @Transactional
    public void updateReview(Long reviewId, ReviewUpdateRequest request,User user){
        Review review = this.reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰가 없습니다."));

        if(!review.getUser().equals(user)){
            throw new AccessDeniedException("주문자만 리뷰를 변경 할 수 있습니다");
        }

        review.changeInfo(request);
    }

    @Transactional
    public void deleteReview(Long reviewId, User user){
        Review review = this.reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰가 없습니다."));

        if(!review.getUser().equals(user)){
            throw new AccessDeniedException("주문자만 리뷰를 삭제 할 수 있습니다");
        }

        review.delete();
    }
}
