package com.hospital.boot.app.notice.controller;

import com.hospital.boot.domain.notice.model.service.NoticeService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService nService;

    /**
     * 공지사항 조회수 증가
     * @param noticeId 공지사항 ID
     * @return 조회수 증가 성공 여부
     */
    @PostMapping("/{noticeId}/view")
    public Map<String, Object> incrementNoticeView(@PathVariable int noticeId) {
        Map<String, Object> response = new HashMap<>();

        try {
            int result = nService.incrementViews(noticeId);

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
