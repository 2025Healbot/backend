package com.hospital.boot.domain.community.model.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityReport {
	private Long reportId;
    private String targetType;   
    private Long postId;
    private Long commentId;
    private String reporterId;
    private String reasonType;
    private String detail;
    private String status;       
    private LocalDateTime createdAt;
}
