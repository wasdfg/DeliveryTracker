package com.example.deliverytracker.review.service;

import com.example.deliverytracker.review.entity.Review;
import com.example.deliverytracker.review.entity.ReviewReply;
import com.example.deliverytracker.review.repository.ReplyRepository;
import com.example.deliverytracker.review.repository.ReviewRepository;
import com.example.deliverytracker.user.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    private final ReviewRepository reviewRepository;

    @Transactional
    public void createReply(Long reviewId, String content, User owner) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));

        if (!review.getStore().getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("본인 가게의 리뷰에만 답글을 달 수 있습니다.");
        }

        if (replyRepository.existsByReviewId(reviewId)) {
            throw new IllegalStateException("이미 답글이 등록된 리뷰입니다.");
        }

        ReviewReply reply = new ReviewReply(content, review, owner);
        replyRepository.save(reply);
    }
}