package com.hospital.boot.domain.notice.model.service.impl;

import com.hospital.boot.domain.notice.model.service.NoticeService;
import com.hospital.boot.domain.notice.model.mapper.NoticeMapper;
import com.hospital.boot.domain.notice.model.vo.Notice;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper nMapper;

    // 공지사항 목록 조회 (NoticeService)
    @Override
    public List<Notice> getAllNotices() {
        return nMapper.getAllNotices();
    }

    // 공지사항 조회수 증가 (NoticeService)
    @Override
	public int incrementViews(int noticeNo) {
	    return nMapper.incrementViews(noticeNo);
	}

    // 관리자 공지사항 등록 (NoticeService)
	@Override
    public int createNotice(Notice notice) {
        return nMapper.insertNotice(notice);
    }

	// 관리자 공지사항 수정 (NoticeService)
    @Override
    public int updateNotice(Notice notice) {
        return nMapper.updateNotice(notice);
    }

    // 관리자 공지사항 삭제 (NoticeService)
    @Override
    public int deleteNotice(int noticeNo) {
        return nMapper.deleteNotice(noticeNo);
    }
}