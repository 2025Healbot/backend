// src/main/java/com/hospital/boot/domain/review/model/service/ReviewService.java
package com.hospital.boot.domain.review.model.service;

import java.util.List;

import com.hospital.boot.app.review.dto.HospitalSearchDto;
import com.hospital.boot.app.review.dto.ReviewListResponse;
import com.hospital.boot.domain.review.model.vo.Review;

public interface ReviewService {

    /**
     * 병원별 리뷰 목록 조회
     *
     * @param hospitalId 병원 ID
     * @param sort       latest/high/low
     * @param rating     "all" 또는 "1"~"5"
     */
    List<Review> findReviews(String hospitalId, String sort, String rating);

    /**
     * 병원별 평균 평점
     */
    double getAvgScore(String hospitalId);

    /**
     * 병원별 리뷰 개수
     */
    int getTotalCount(String hospitalId);

    /**
     * 병원 이름 조회 (헤더에 보여줄 용도)
     */
    String getHospitalName(String hospitalId);

    /**
     * 리뷰 등록
     */
    int insertReview(Review review);

	ReviewListResponse getAllReviews(String sort, String rating);

	/**
     * 내가 쓴 리뷰만 조회
     */
    List<Review> getMyReviews(String memberId, String sort, String rating);

	/**
     * 리뷰 삭제 (본인 확인)
     */
    int deleteReview(String reviewId, String memberId);

	/**
     * 리뷰 삭제 (관리자용 - 본인 확인 없음)
     */
    int deleteReviewByAdmin(String reviewId);

	/**
     * 내가 쓴 리뷰 개수
     */
    int getMyReviewCount(String memberId);

	/**
     * 병원명 키워드로 검색
     */
    List<HospitalSearchDto> searchHospitals(String keyword);
}
