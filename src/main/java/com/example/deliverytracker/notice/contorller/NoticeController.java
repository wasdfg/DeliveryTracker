package com.example.deliverytracker.notice.contorller;

import com.example.deliverytracker.notice.dto.NoticeCreateRequest;
import com.example.deliverytracker.notice.dto.NoticeResponse;
import com.example.deliverytracker.notice.dto.NoticeSearchCondition;
import com.example.deliverytracker.notice.dto.NoticeUpdateRequest;
import com.example.deliverytracker.notice.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/notice")
@RestController
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<Page<NoticeResponse>> getNotices(NoticeSearchCondition noticeSearchCondition, Pageable pageable){

        return ResponseEntity.ok(this.noticeService.getNotices(noticeSearchCondition,pageable));
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> getNoticeDetail(@PathVariable Long noticeId){

        return ResponseEntity.ok(this.noticeService.getNoticeDetail(noticeId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> registerNotice(@Valid @RequestBody NoticeCreateRequest noticeCreateRequest){

        this.noticeService.registerNotice(noticeCreateRequest);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<?> deleteNotice(@PathVariable Long noticeId){

        this.noticeService.deleteNotice(noticeId);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{noticeId}/restore")
    public ResponseEntity<?> restoreNotice(@PathVariable Long noticeId, @RequestParam boolean visible){

        this.noticeService.restoreNotice(noticeId, visible);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{noticeId}/change")
    public ResponseEntity<?> changeNotice(@PathVariable Long noticeId, @Valid @RequestBody NoticeUpdateRequest noticeUpdateRequest){

        this.noticeService.changeNotice(noticeId, noticeUpdateRequest);

        return ResponseEntity.ok().build();
    }
}
