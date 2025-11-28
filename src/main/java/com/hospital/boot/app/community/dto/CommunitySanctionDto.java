package com.hospital.boot.app.community.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CommunitySanctionDto {
	private Long reportId;        // REPORT_ID
    private String targetType;    // POST / COMMENT
    private Long postId;          // POST_ID
    private Long commentId;       // COMMENT_ID

    private String reporterId;    // REPORTER_ID (신고자)
    private String reasonType;    // REASON_TYPE
    private String detail;        // DETAIL
    private String status;        // STATUS
    private String penaltyReason; // PENALTY_REASON (제재·처리 결과)

    private String targetSummary; // 게시글 제목 or 댓글 내용 일부
    private String createdAt;     // 신고일시 (문자열로 포맷)
}
