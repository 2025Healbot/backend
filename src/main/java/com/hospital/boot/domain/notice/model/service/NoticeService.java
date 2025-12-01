package com.hospital.boot.domain.notice.model.service;

import com.hospital.boot.domain.notice.model.vo.Notice;
import java.util.List;

public interface NoticeService {

	// 공지사항 목록 조회 (NoticeController)
    List<Notice> getAllNotices();
    
    // 공지사항 조회수 증가 (NoticeController)
    int incrementViews(int noticeNo);

    // 관리자 공지사항 등록 (AdminController)
    int createNotice(Notice notice);

    // 관리자 공지사항 수정 (AdminController)
    int updateNotice(Notice notice);

    // 관리자 공지사항 삭제 (AdminController)
    int deleteNotice(int noticeNo);
}
