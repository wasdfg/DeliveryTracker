package com.example.deliverytracker.review.controller;

import com.example.deliverytracker.review.dto.ReplyRequest;
import com.example.deliverytracker.review.service.ReplyService;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/{reviewId}/reply")
    public ResponseEntity<String> createReply(@PathVariable Long reviewId, @RequestBody ReplyRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        replyService.createReply(reviewId, request.getContent(), userDetails.getUser());

        return ResponseEntity.ok("답글이 성공적으로 등록되었습니다.");
    }

    @PutMapping("/{reviewId}/reply")
    public ResponseEntity<String> updateReply(@PathVariable Long reviewId, @RequestBody ReplyRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        replyService.updateReply(reviewId, request.getContent(), userDetails.getUser());
        return ResponseEntity.ok("답글이 수정되었습니다.");
    }

    @DeleteMapping("/{reviewId}/reply")
    public ResponseEntity<String> deleteReply(@PathVariable Long reviewId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        replyService.deleteReply(reviewId, userDetails.getUser());
        return ResponseEntity.ok("답글이 삭제되었습니다.");
    }
}
