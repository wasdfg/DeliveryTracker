package com.example.deliverytracker.review.dto;

import com.example.deliverytracker.review.entity.ReviewReply;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReplyResponse {
    private String content;
    private LocalDateTime createdAt;

    public ReplyResponse(ReviewReply reply) {
        this.content = reply.getContent();
        this.createdAt = reply.getCreatedAt();
    }
}
