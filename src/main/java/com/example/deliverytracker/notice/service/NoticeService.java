package com.example.deliverytracker.notice.service;

import com.example.deliverytracker.notice.dto.NoticeCreateRequest;
import com.example.deliverytracker.notice.dto.NoticeResponse;
import com.example.deliverytracker.notice.dto.NoticeSearchCondition;
import com.example.deliverytracker.notice.dto.NoticeUpdateRequest;
import com.example.deliverytracker.notice.entity.Notice;
import com.example.deliverytracker.notice.repository.NoticeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Page<NoticeResponse> getNotices(NoticeSearchCondition noticeSearchCondition, Pageable pageable){

        Page<Notice> notices = this.noticeRepository.searchNotices(noticeSearchCondition, pageable);

        return notices.map(NoticeResponse::from);
    }

    public NoticeResponse getNoticeDetail(Long noticeId){

        Notice notice = this.noticeRepository.findById(noticeId).orElseThrow(() -> new EntityNotFoundException("공지를 찾을 수 없습니다."));;

        return NoticeResponse.from(notice);
    }

    @Transactional
    public void registerNotice(NoticeCreateRequest noticeCreateRequest){
        Notice notice = new Notice(noticeCreateRequest.getTitle(), noticeCreateRequest.getContent(), noticeCreateRequest.isImportant());

        this.noticeRepository.save(notice);
    }

    @Transactional
    public void deleteNotice(Long noticeId){
        Notice notice = this.noticeRepository.findById(noticeId).orElseThrow(() -> new EntityNotFoundException("공지를 찾을 수 없습니다."));

        notice.delete();
    }


    @Transactional
    public void restoreNotice(Long noticeId, boolean visible){
        Notice notice = this.noticeRepository.findById(noticeId).orElseThrow(() -> new EntityNotFoundException("공지를 찾을 수 없습니다."));

        notice.restore(visible);
    }

    @Transactional
    public void changeNotice(Long noticeId, NoticeUpdateRequest noticeUpdateRequest){
        Notice notice = this.noticeRepository.findById(noticeId).orElseThrow(() -> new EntityNotFoundException("공지를 찾을 수 없습니다."));

        notice.update(noticeUpdateRequest.getTitle(), noticeUpdateRequest.getContent(), noticeUpdateRequest.isImportant(), noticeUpdateRequest.isVisible());
    }
}
