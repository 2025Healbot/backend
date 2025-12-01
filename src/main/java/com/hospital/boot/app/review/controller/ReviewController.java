// src/main/java/com/hospital/boot/app/review/controller/ReviewController.java
package com.hospital.boot.app.review.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.hospital.boot.app.member.dto.CommonResponse;
import com.hospital.boot.app.review.dto.ReviewListResponse;
import com.hospital.boot.domain.review.model.service.ReviewService;
import com.hospital.boot.domain.review.model.vo.Review;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService rService;

    /**
     * 리뷰 목록 조회
     *  GET /api/reviews?hospitalId=...&sort=latest&rating=all
     */
    @GetMapping
    public ResponseEntity<?> getReviews(
            @RequestParam String hospitalId,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "all") String rating
    ) {
        try {
            List<Review> list = rService.findReviews(hospitalId, sort, rating);
            double avgScore = rService.getAvgScore(hospitalId);
            int totalCount = rService.getTotalCount(hospitalId);
            String hospitalName = rService.getHospitalName(hospitalId);

            ReviewListResponse dto = new ReviewListResponse();
            dto.setHospitalName(hospitalName);
            dto.setAvgScore(avgScore);
            dto.setTotalCount(totalCount);
            dto.setReviewList(list);

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponse(false, "리뷰 조회 중 오류가 발생했습니다."));
        }
    }

    /**
     * 리뷰 등록
     *  POST /api/reviews (multipart/form-data)
     *  - hospitalId, score, content, files[]
     */
    @PostMapping
    public ResponseEntity<?> createReview(
            @RequestParam("hospitalId") String hospitalId,
            @RequestParam("score") int score,
            @RequestParam("content") String content,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            HttpSession session
    ) {
        try {
            // 로그인 체크
            String memberId = (String) session.getAttribute("memberId");
            if (memberId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new CommonResponse(false, "로그인이 필요합니다."));
            }

            // 리뷰 VO 세팅
            Review review = new Review();
            review.setHospitalId(hospitalId);
            review.setMemberId(memberId);
            review.setScore(score);
            review.setContent(content);

            int result = rService.insertReview(review);
            if (result <= 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new CommonResponse(false, "리뷰 등록에 실패했습니다."));
            }

            // 이미지(files)는 나중에 별도 테이블/저장소에 붙이면 됨. 지금은 무시하거나 TODO 처리.
            // TODO: files 처리 로직 (파일 저장 + REVIEW_IMAGES 테이블 등)

            return ResponseEntity.ok(new CommonResponse(true, "리뷰가 등록되었습니다."));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponse(false, "리뷰 등록 중 오류가 발생했습니다."));
        }
    }
}
