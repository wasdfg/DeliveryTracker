package com.example.deliverytracker.notice.repository;

import com.example.deliverytracker.notice.dto.NoticeSearchCondition;
import com.example.deliverytracker.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<Notice> searchNotices(NoticeSearchCondition condition, Pageable pageable);
}