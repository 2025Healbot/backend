package com.hospital.boot.app.member.controller;

import com.hospital.boot.app.member.dto.LoginResponse;
import com.hospital.boot.domain.member.model.service.MemberService;
import com.hospital.boot.domain.member.model.vo.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService mService;

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> socialLogin(
            @RequestParam String type,
            @RequestParam String code,
            HttpSession session) {

        try {
            // 1. 서비스에서 소셜 API 호출하여 소셜 사용자 ID 가져오기
            String userId = mService.getSocialUserId(type, code);

            // 2. 소셜 ID 생성 (type_userId 형식)
            String socialId = type + "_" + userId;

            // 3. DB에서 해당 소셜 ID로 회원 조회
            Member member = mService.findBySocialId(socialId);

            LoginResponse response = new LoginResponse();
            response.setSocialId(socialId);

            if (member != null) {
                // 로그인 성공 - 세션에 회원 ID 저장
                session.setAttribute("memberId", member.getMemberId());
                response.setSuccess(1);
            } else {
                // 회원가입 필요 - 소셜 ID만 반환
                response.setSuccess(0);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/signup")
    public int signup(@RequestBody Member member) {
        return mService.signup(member);
    }

}
