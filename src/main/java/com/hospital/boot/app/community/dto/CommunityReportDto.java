package com.hospital.boot.app.community.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class CommunityReportDto {
    private Long reportId;
    private String targetType;      // POST, COMMENT
    private Long postId;
    private Long commentId;
    private String reporterId;
    private String reporterName;    // 신고자 이름
    private String reasonType;      // 신고 사유 유형
    private String detail;          // 상세 내용
    private String status;          // PENDING, RESOLVED, REJECTED
    private String reply;           // 답변
    private LocalDateTime createdAt;

    // 게시글/댓글 정보
    private String postTitle;       // 신고된 게시글 제목
    private String postStatus;      // 게시글 상태 (ACTIVE, HIDDEN)
    private String commentContent;  // 신고된 댓글 내용
    private String commentStatus;   // 댓글 상태 (ACTIVE, HIDDEN)
    private String targetAuthorId;  // 신고 대상 작성자 ID
}
