// src/main/java/com/hospital/boot/domain/review/model/service/impl/ReviewServiceImpl.java
package com.hospital.boot.domain.review.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

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
}
