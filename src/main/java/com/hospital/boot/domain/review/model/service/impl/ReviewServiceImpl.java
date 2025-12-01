// src/main/java/com/hospital/boot/domain/review/model/service/impl/ReviewServiceImpl.java
package com.hospital.boot.domain.review.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hospital.boot.app.review.dto.ReviewListResponse;
import com.hospital.boot.domain.review.model.mapper.ReviewMapper;
import com.hospital.boot.domain.review.model.service.ReviewService;
import com.hospital.boot.domain.review.model.vo.Review;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper rMapper;

    @Override
    public List<Review> findReviews(String hospitalId, String sort, String rating) {

        Integer scoreFilter = null;
        if (rating != null && !"all".equalsIgnoreCase(rating)) {
            try {
                scoreFilter = Integer.parseInt(rating);
            } catch (NumberFormatException ignored) {
            }
        }

        return rMapper.findReviews(hospitalId, sort, scoreFilter);
    }

    @Override
    public double getAvgScore(String hospitalId) {
        Double avg = rMapper.getAvgScore(hospitalId);
        return (avg == null) ? 0.0 : avg;
    }

    @Override
    public int getTotalCount(String hospitalId) {
        return rMapper.getTotalCount(hospitalId);
    }

    @Override
    public String getHospitalName(String hospitalId) {
        return rMapper.getHospitalName(hospitalId);
    }

    @Override
    public int insertReview(Review review) {
        return rMapper.insertReview(review);
    }

	@Override
	public ReviewListResponse getAllReviews(String sort, String rating) {
		Integer scoreFilter = null;
	    if (!"all".equals(rating)) {
	        scoreFilter = Integer.valueOf(rating);
	    }

	    List<Review> list = rMapper.findAllReviews(sort, scoreFilter);

	    ReviewListResponse res = new ReviewListResponse();
	    res.setReviewList(list);
	    // 전체 평균, 전체 개수 등 넣고 싶으면 여기서 계산
	    return res;
	}
}
