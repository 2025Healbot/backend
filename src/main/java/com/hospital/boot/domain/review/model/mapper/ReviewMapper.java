// src/main/java/com/hospital/boot/domain/review/model/mapper/ReviewMapper.java
package com.hospital.boot.domain.review.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hospital.boot.domain.review.model.vo.Review;

@Mapper
public interface ReviewMapper {

    List<Review> findReviews(
            @Param("hospitalId") String hospitalId,
            @Param("sort") String sort,
            @Param("scoreFilter") Integer scoreFilter
    );

    Double getAvgScore(@Param("hospitalId") String hospitalId);

    int getTotalCount(@Param("hospitalId") String hospitalId);

    String getHospitalName(@Param("hospitalId") String hospitalId);

    int insertReview(Review review);
}
