// src/main/java/com/hospital/boot/domain/review/model/vo/Review.java
package com.hospital.boot.domain.review.model.vo;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Review {

    private Long reviewId;     // REVIEW_ID
    private String hospitalId; // HOSPITAL_ID (FK -> HOSPITALS)
    private String memberId;   // MEMBER_ID (FK -> MEMBER)
    private int score;         // SCORE
    private String content;    // CONTENT
    private Date createdAt;    // CREATED_AT

    // 조회용(조인 결과) – 테이블 컬럼은 아니지만 MyBatis에서 매핑해서 씀
    private String writerName; // MEMBER.USER_NAME
    private String hospitalName;  // h.HOSPITAL_NAME
}
