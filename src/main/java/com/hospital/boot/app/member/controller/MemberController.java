package com.hospital.boot.app.member.controller;

import com.hospital.boot.app.member.dto.CommonResponse;
import com.hospital.boot.app.member.dto.LoginResponse;
import com.hospital.boot.app.member.dto.ProfileResponse;
import com.hospital.boot.app.member.dto.ProfileUpdateRequest;
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
    
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpSession session) {
        // ✅ 로그인된 회원 PK를 세션에서 꺼냄 (위 로그인 로직과 동일한 key 사용)
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse(false, "로그인이 필요합니다."));
        }

        // MemberService 사용 시 이름은 mService
        Member member = mService.findById(memberId);   // 이 메서드는 Service에 새로 추가 필요
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(false, "회원 정보를 찾을 수 없습니다."));
        }

        ProfileResponse dto = new ProfileResponse();
        dto.setUserName(member.getUserName());
        dto.setEmail(member.getEmail());
        dto.setPhone(member.getPhone());
        dto.setBornDate(member.getBornDate()); // String/LocalDate 에 맞게 변환
        dto.setGender(member.getGender());
        dto.setAddress(member.getAddress());
        
        if (member.getCreatedAt() != null) {
            dto.setJoinDate(member.getCreatedAt());   // String 이니까 toString() 안 해도 됨
        }

        // 프론트 Profile.jsx는 success 안 보고 바로 필드만 읽으니까
        // 여기서는 순수 프로필 정보만 내려주면 됨
        return ResponseEntity.ok(dto);
    }

    // ================= 프로필 수정 =================
    @PutMapping("/profile")
    public ResponseEntity<CommonResponse> updateProfile(
            HttpSession session,
            @RequestBody ProfileUpdateRequest request) {

        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse(false, "로그인이 필요합니다."));
        }

        try {
            // 이 부분도 Service에 구현 필요
            mService.updateProfile(memberId, request);

            // 프론트에서는 data.success === false 만 체크하니까 true면 OK
            return ResponseEntity.ok(new CommonResponse(true, "프로필이 수정되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new CommonResponse(false, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponse(false, "프로필 수정 중 오류가 발생했습니다."));
        }
    }
}
