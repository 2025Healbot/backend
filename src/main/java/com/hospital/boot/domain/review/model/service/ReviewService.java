// src/main/java/com/hospital/boot/domain/review/model/service/ReviewService.java
package com.hospital.boot.domain.review.model.service;

import java.util.List;

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
}
