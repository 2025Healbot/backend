// com.hospital.boot.app.review.dto.ReviewDto.java
package com.hospital.boot.app.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {
    private Long   reviewId;
    private String hospitalId;
    private String memberId;
    private String writerName;
    private int    score;
    private String content;
    private String createdAt;   // 화면 표시용 (yyyy-MM-dd HH:mm 등으로 포맷해서 가져와도 됨)
}
