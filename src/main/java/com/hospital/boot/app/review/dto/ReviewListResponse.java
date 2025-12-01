// src/main/java/com/hospital/boot/app/review/dto/ReviewListResponse.java
package com.hospital.boot.app.review.dto;

import java.util.List;

import com.hospital.boot.domain.review.model.vo.Review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewListResponse {
    private String hospitalName;
    private double avgScore;
    private int totalCount;
    private List<Review> reviewList;
}
