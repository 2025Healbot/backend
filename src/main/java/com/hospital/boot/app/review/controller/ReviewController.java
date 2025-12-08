// src/main/java/com/hospital/boot/app/review/controller/ReviewController.java
package com.hospital.boot.app.review.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.hospital.boot.app.member.dto.CommonResponse;
import com.hospital.boot.app.review.dto.HospitalSearchDto;
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
            @RequestBody Review review,
            HttpSession session
    ) {
        try {
            // 로그인 체크
            String memberId = (String) session.getAttribute("memberId");
            if (memberId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new CommonResponse(false, "로그인이 필요합니다."));
            }
            review.setMemberId(memberId);
            System.out.println("받은 리뷰 데이터: " + review);
            int result = rService.insertReview(review);
            if (result <= 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new CommonResponse(false, "리뷰 등록에 실패했습니다."));
            }
            // TODO: files 처리 로직 (파일 저장, DB에 파일 정보 저장 등)
            return ResponseEntity.ok(new CommonResponse(true, "리뷰가 등록되었습니다."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponse(false, "리뷰 등록 중 오류가 발생했습니다."));
        }
    }
    
    @GetMapping("/all")
    public ReviewListResponse getAllReviews(
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "all") String rating) {
        return rService.getAllReviews(sort, rating);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<HospitalSearchDto>> searchHospitals(
            @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        List<HospitalSearchDto> list = rService.searchHospitals(keyword);
        return ResponseEntity.ok(list);
    }

    /**
     * 내가 쓴 리뷰만 조회
     * GET /api/reviews/my?sort=latest&rating=all
     */
    @GetMapping("/my")
    public ResponseEntity<?> getMyReviews(
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "all") String rating,
            HttpSession session
    ) {
        try {
            // 로그인 체크
            String memberId = (String) session.getAttribute("memberId");
            if (memberId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new CommonResponse(false, "로그인이 필요합니다."));
            }

            List<Review> list = rService.getMyReviews(memberId, sort, rating);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponse(false, "리뷰 조회 중 오류가 발생했습니다."));
        }
    }

    /**
     * 리뷰 삭제
     * DELETE /api/reviews/{reviewId}
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable String reviewId,
            HttpSession session
    ) {
        try {
            // 로그인 체크
            String memberId = (String) session.getAttribute("memberId");
            if (memberId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new CommonResponse(false, "로그인이 필요합니다."));
            }

            // TODO: 본인의 리뷰인지 확인 후 삭제
            int result = rService.deleteReview(reviewId, memberId);
            if (result > 0) {
                return ResponseEntity.ok(new CommonResponse(true, "리뷰가 삭제되었습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new CommonResponse(false, "본인의 리뷰만 삭제할 수 있습니다."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponse(false, "리뷰 삭제 중 오류가 발생했습니다."));
        }
    }

    /**
     * 내가 쓴 리뷰 개수
     * GET /api/reviews/my-count
     */
    @GetMapping("/my-count")
    public ResponseEntity<?> getMyReviewCount(HttpSession session) {
        try {
            String memberId = (String) session.getAttribute("memberId");
            if (memberId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new CommonResponse(false, "로그인이 필요합니다."));
            }

            int count = rService.getMyReviewCount(memberId);
            return ResponseEntity.ok(java.util.Map.of(
                    "success", true,
                    "count", count
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponse(false, "개수 조회 중 오류가 발생했습니다."));
        }
    }

    /**
     * 리뷰 삭제 (관리자용)
     * DELETE /api/reviews/admin/{reviewId}
     */
    @DeleteMapping("/admin/{reviewId}")
    public ResponseEntity<?> deleteReviewByAdmin(
            @PathVariable String reviewId,
            HttpSession session
    ) {
        try {
            // 로그인 체크
            String memberId = (String) session.getAttribute("memberId");
            if (memberId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new CommonResponse(false, "로그인이 필요합니다."));
            }

            // 관리자 권한 체크
            String adminYn = (String) session.getAttribute("adminYn");
            if (!"Y".equals(adminYn)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new CommonResponse(false, "관리자 권한이 필요합니다."));
            }

            // 관리자는 본인 확인 없이 삭제 가능
            int result = rService.deleteReviewByAdmin(reviewId);
            if (result > 0) {
                return ResponseEntity.ok(new CommonResponse(true, "리뷰가 삭제되었습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new CommonResponse(false, "리뷰를 찾을 수 없습니다."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponse(false, "리뷰 삭제 중 오류가 발생했습니다."));
        }
    }
}
