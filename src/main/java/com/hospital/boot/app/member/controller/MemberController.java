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

    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkId(@RequestParam String id) {
        boolean available = mService.checkId(id);
        return ResponseEntity.ok(available);
    }

    @GetMapping("/find-id")
    public String findId(@RequestParam String name
            ,@RequestParam String email) {
        return mService.findId(name, email);
    }

    @PostMapping("/normal-login")
    public ResponseEntity<LoginResponse> normalLogin(
            @RequestParam String memberId,
            @RequestParam String password,
            HttpSession session) {

        try {
            Member member = mService.normalLogin(memberId, password);

            LoginResponse response = new LoginResponse();

            if (member != null) {
                // 로그인 성공 - 세션에 회원 ID 저장
                session.setAttribute("memberId", member.getMemberId());
                response.setSuccess(1);
            } else {
                // 로그인 실패
                response.setSuccess(0);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        boolean loggedIn = (memberId != null);

        String adminYn = "N"; // 기본값
        if (loggedIn) {
            // 로그인 되어있으면 DB에서 회원 정보 조회하여 admin_YN 가져오기
            Member member = mService.findByMemberId(memberId);
            if (member != null && member.getAdminYn() != null) {
                adminYn = member.getAdminYn();
            }
        }

        String finalAdminYn = adminYn;
        return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
            put("loggedIn", loggedIn);
            put("admin_YN", finalAdminYn);
        }});
    }

    @PostMapping("/verify-id-email")
    public ResponseEntity<?> verifyIdAndEmail(
            @RequestParam String memberId,
            @RequestParam String email) {

        try {
            boolean verified = mService.verifyIdAndEmail(memberId, email);

            return ResponseEntity.ok(new java.util.HashMap<String, Boolean>() {{
                put("verified", verified);
            }});

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String memberId,
            @RequestParam String password) {

        try {
            boolean success = mService.resetPassword(memberId, password);

            return ResponseEntity.ok(new java.util.HashMap<String, Boolean>() {{
                put("success", success);
            }});

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        try {
            // 세션 무효화
            session.invalidate();

            return ResponseEntity.ok(new java.util.HashMap<String, Boolean>() {{
                put("success", true);
            }});

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
