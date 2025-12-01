package com.hospital.boot.app.notice.controller;

import com.hospital.boot.domain.notice.model.service.NoticeService;
import com.hospital.boot.domain.notice.model.vo.Notice;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService nService;

    // 공지사항 목록 조회
    @GetMapping
    public List<Notice> getAllNotices() {
        return nService.getAllNotices();
    }
    
    // 공지사항 조회수 증가
    @PostMapping("/{noticeNo}/view")
    public Map<String, Object> incrementNoticeView(@PathVariable int noticeNo) {
        Map<String, Object> response = new HashMap<>();

        try {
            int result = nService.incrementViews(noticeNo);

            if (result > 0) {
                response.put("success", true);
                response.put("message", "조회수가 증가되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "조회수 증가에 실패했습니다.");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "조회수 증가 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return response;
    }
}