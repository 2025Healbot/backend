package com.hospital.boot.app.community.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

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
    // 📌 게시글 상세
    // =======================
    @GetMapping("/posts/{postId}")
    public ResponseEntity<CommunityPostDto> getPost(@PathVariable Long postId) {
        CommunityPostDto dto = cService.getPostDetail(postId);
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
}