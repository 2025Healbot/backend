package com.hospital.boot.domain.community.model.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityReport {
	private Long reportId;				// 신고ID
    private String targetType;			// 글 종류
    private Long postId;				// 커뮤니티 게시글ID
    private Long commentId;				// 커뮤니티 댓글ID
    private String reporterId;			// 신고자ID
    private String reasonType;			// 신고 종류
    private String detail;				// 신고 상세 내용
    private String status;				// 신고 상태
    private LocalDateTime createdAt;	// 등록일
    private String reply;				// 관리자 답변
}
