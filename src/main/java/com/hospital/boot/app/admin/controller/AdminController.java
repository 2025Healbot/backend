package com.hospital.boot.app.admin.controller;

import com.hospital.boot.domain.admin.model.service.AdminService;
import com.hospital.boot.domain.member.model.vo.Member;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService aService;

    /**
     * 전체 회원 목록 조회
     * @return 회원 목록
     */
    @GetMapping("/members")
    public List<Member> getAllMembers() {
        return aService.getAllMembers();
    }

    /**
     * 회원 삭제
     * @param memberId 삭제할 회원 ID
     * @return 삭제 성공 여부
     */
    @DeleteMapping("/members/{memberId}")
    public Map<String, Object> deleteMember(@PathVariable String memberId) {
        Map<String, Object> response = new HashMap<>();

        int result = aService.deleteMember(memberId);

        if (result > 0) {
            response.put("success", true);
            response.put("message", "회원이 삭제되었습니다.");
        } else {
            response.put("success", false);
            response.put("message", "회원 삭제에 실패했습니다.");
        }

        return response;
    }

}
