package com.hospital.boot.domain.notice.model.mapper;

import com.hospital.boot.domain.notice.model.vo.Notice;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface NoticeMapper {

	// 공지사항 목록 조회
    List<Notice> getAllNotices();
    
    // 공지사항 조회수 증가
    int incrementViews(int noticeNo);

    // 관리자 공지사항 등록
    int insertNotice(Notice notice);

    // 관리자 공지사항 수정
    int updateNotice(Notice notice);

    // 관리자 공지사항 삭제
    int deleteNotice(int noticeNo);
}