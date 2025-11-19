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

    @Override
    public List<Notice> getAllNotices() {
        return nMapper.getAllNotices();
    }

    @Override
    public int createNotice(Notice notice) {
        return nMapper.insertNotice(notice);
    }

    @Override
    public int updateNotice(Notice notice) {
        return nMapper.updateNotice(notice);
    }

    @Override
    public int deleteNotice(int noticeId) {
        return nMapper.deleteNotice(noticeId);
    }

    @Override
    public int incrementViews(int noticeId) {
        return nMapper.incrementViews(noticeId);
    }
}
