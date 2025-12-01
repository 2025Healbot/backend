package com.hospital.boot.app.community.controller;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hospital.boot.app.community.dto.*;
import com.hospital.boot.domain.community.model.service.CommunityService;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService cService;

    // =======================
    // 📌 게시글 목록
    // =======================
    @GetMapping("/posts")
    public ResponseEntity<List<CommunityPostDto>> listPosts(
            @RequestParam(name = "category", defaultValue = "all") String category,
            @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        List<CommunityPostDto> list = cService.getPostList(category, keyword);
        return ResponseEntity.ok(list);
    }

    // =======================
    // 📌 게시글 상세 + 조회수
    // =======================
    @GetMapping("/posts/{postId}")
    public ResponseEntity<CommunityPostDto> getPost(
            @PathVariable Long postId,
            @RequestParam(name = "admin", defaultValue = "false") boolean isAdmin) {

        CommunityPostDto dto;
        if (isAdmin) {
            // 관리자 조회 시 조회수 증가 안 함
            dto = cService.getPostDetail(postId);
        } else {
            // 일반 사용자 조회 시 조회수 증가
            dto = cService.viewPost(postId);
        }

        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    // =======================
    // 📌 게시글 작성
    // =======================
    @PostMapping("/posts")
    public ResponseEntity<Long> writePost(
            @RequestBody CommunityPostWriteRequest req,
            HttpSession session) {

        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(401).build();
        }

        Long postId = cService.writePost(memberId, req);
        return ResponseEntity.ok(postId);
    }

    // =======================
    // 📌 댓글 목록
    // =======================
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommunityCommentDto>> listComments(@PathVariable Long postId) {
        return ResponseEntity.ok(cService.getComments(postId));
    }

    // =======================
    // 📌 댓글 작성
    // =======================
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Long> writeComment(
            @PathVariable Long postId,
            @RequestBody CommunityCommentWriteRequest req,
            HttpSession session) {

        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(401).build();
        }

        Long id = cService.writeComment(memberId, postId, req);
        return ResponseEntity.ok(id);
    }
    // ========================
    // 게시물 수정
    // ========================
    @PutMapping("/posts/{postId}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long postId,
            @RequestBody CommunityPostWriteRequest req,
            @RequestParam(name = "admin", defaultValue = "false") boolean isAdmin,
            HttpSession session) {

        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        // 관리자는 권한 체크 없이 수정 가능
        if (!isAdmin) {
            // 일반 사용자는 본인 글만 수정 가능
            CommunityPostDto post = cService.getPostDetail(postId);
            if (post == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "message", "게시글을 찾을 수 없습니다."));
            }
            if (!post.getMemberId().equals(memberId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("success", false, "message", "본인이 작성한 글만 수정할 수 있습니다."));
            }
        }

        boolean updated = cService.updatePost(postId, req);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "게시글 수정에 실패했습니다."));
        }

        return ResponseEntity.ok(Map.of("success", true));
    }

    // ========================
    // 게시물 삭제
    // ========================
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long postId,
            @RequestParam(name = "admin", defaultValue = "false") boolean isAdmin,
            HttpSession session) {

        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        boolean deleted;
        if (isAdmin) {
            // 관리자는 모든 게시글 삭제 가능
            deleted = cService.adminDeletePost(postId);
        } else {
            // 일반 사용자는 본인 글만 삭제 가능
            deleted = cService.deletePost(postId, memberId);
        }

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "message", "본인이 작성한 글만 삭제할 수 있습니다."));
        }

        return ResponseEntity.ok(Map.of("success", true));
    }
    
    // 🔹 내가 쓴 게시글 개수
    @GetMapping("/my-post-count")
    public ResponseEntity<?> getMyPostCount(HttpSession session) {

        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(401)
                    .body(java.util.Map.of(
                            "success", false,
                            "message", "로그인이 필요합니다."
                    ));
        }

        int count = cService.getMyPostCount(memberId);

        return ResponseEntity.ok(java.util.Map.of(
                "success", true,
                "count", count
        ));
    }
    
    // 🔴 게시글 신고
    @PostMapping("/posts/{postId}/report")
    public ResponseEntity<?> reportPost(
            @PathVariable Long postId,
            @RequestBody CommunityReportRequest req,
            HttpSession session
    ) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "로그인이 필요합니다."
            ));
        }

        cService.reportPost(memberId, postId, req);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "신고가 접수되었습니다."
        ));
    }

    // 🔴 댓글 신고
    @PostMapping("/comments/{commentId}/report")
    public ResponseEntity<?> reportComment(
            @PathVariable Long commentId,
            @RequestBody CommunityReportRequest req,
            HttpSession session
    ) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "로그인이 필요합니다."
            ));
        }

        cService.reportComment(memberId, commentId, req);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "신고가 접수되었습니다."
        ));
    }

    // =======================
    // 📌 신고 목록 조회
    // =======================
    @GetMapping("/reports")
    public ResponseEntity<List<CommunityReportDto>> listReports(
            @RequestParam(name = "status", defaultValue = "") String status,
            @RequestParam(name = "targetType", defaultValue = "") String targetType) {

        List<CommunityReportDto> list = cService.getReportList(status, targetType);
        return ResponseEntity.ok(list);
    }

    // =======================
    // 📌 신고 상세 조회
    // =======================
    @GetMapping("/reports/{reportId}")
    public ResponseEntity<CommunityReportDto> getReport(@PathVariable Long reportId) {
        CommunityReportDto dto = cService.getReportDetail(reportId);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    // =======================
    // 📌 제재사유 입력
    // =======================
    @PutMapping("/reports/{reportId}/penalty")
    public ResponseEntity<?> submitPenalty(
            @PathVariable Long reportId,
            @RequestBody Map<String, String> body,
            HttpSession session) {

        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        String reply = body.get("reply");
        if (reply == null || reply.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "답변을 입력해주세요."));
        }

        // 답변만 업데이트 (상태 변경 안 함)
        boolean updated = cService.updateReply(reportId, reply);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "답변 등록에 실패했습니다."));
        }

        return ResponseEntity.ok(Map.of("success", true));
    }

    // =======================
    // 📌 신고 삭제
    // =======================
    @DeleteMapping("/reports/{reportId}")
    public ResponseEntity<?> deleteReport(
            @PathVariable Long reportId,
            HttpSession session) {

        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        boolean deleted = cService.deleteReport(reportId);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "신고 삭제에 실패했습니다."));
        }

        return ResponseEntity.ok(Map.of("success", true));
    }

    // =======================
    // 📌 게시글 숨김/해제 토글
    // =======================
    @PutMapping("/posts/{postId}/toggle-visibility")
    public ResponseEntity<?> togglePostVisibility(
            @PathVariable Long postId,
            HttpSession session) {

        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        boolean toggled = cService.togglePostVisibility(postId);
        if (!toggled) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "게시글 상태 변경에 실패했습니다."));
        }

        return ResponseEntity.ok(Map.of("success", true));
    }

    // =======================
    // 📌 댓글 숨김/해제 토글
    // =======================
    @PutMapping("/comments/{commentId}/toggle-visibility")
    public ResponseEntity<?> toggleCommentVisibility(
            @PathVariable Long commentId,
            HttpSession session) {

        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        boolean toggled = cService.toggleCommentVisibility(commentId);
        if (!toggled) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "댓글 상태 변경에 실패했습니다."));
        }

        return ResponseEntity.ok(Map.of("success", true));
    }

    
 // =======================
    // 📌 내가 제재 받은 내역 목록
    // =======================
    @GetMapping("/my-sanctions/received")
    public ResponseEntity<?> getMyReceivedSanctions(HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        List<MySanctionDto> list = cService.getMyReceivedSanctions(memberId);
        return ResponseEntity.ok(list);   // 그대로 배열 리턴
    }

    // =======================
    // 📌 내가 신고해서 제재된 내역 목록
    // =======================
    @GetMapping("/my-sanctions/reported")
    public ResponseEntity<?> getMyReportedSanctions(HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        List<MySanctionDto> list = cService.getMyReportedSanctions(memberId);
        return ResponseEntity.ok(list);
    }

    // =======================
    // 📌 내가 받은 제재 개수 (마이페이지 표시용)
    //   -> 프론트: /react/api/community/my-sanction-count 로 호출
    // =======================
    @GetMapping("/my-sanction-count")
    public ResponseEntity<?> getMySanctionCount(HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        int count = cService.getMySanctionCount(memberId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "count", count
        ));
    }
}