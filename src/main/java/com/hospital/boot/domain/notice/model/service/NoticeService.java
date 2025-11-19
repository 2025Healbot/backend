package com.hospital.boot.domain.notice.model.service;

import com.hospital.boot.domain.notice.model.vo.Notice;
import java.util.List;

public interface NoticeService {

    /**
     * 전체 공지사항 목록 조회
     * @return 공지사항 목록
     */
    List<Notice> getAllNotices();

    /**
     * 공지사항 생성
     * @param notice 공지사항 정보
     * @return 생성된 row 수
     */
    int createNotice(Notice notice);

    /**
     * 공지사항 수정
     * @param notice 공지사항 정보
     * @return 수정된 row 수
     */
    int updateNotice(Notice notice);

    /**
     * 공지사항 삭제
     * @param noticeId 공지사항 ID
     * @return 삭제된 row 수
     */
    int deleteNotice(int noticeId);

    /**
     * 공지사항 조회수 증가
     * @param noticeId 공지사항 ID
     * @return 수정된 row 수
     */
    int incrementViews(int noticeId);
}
