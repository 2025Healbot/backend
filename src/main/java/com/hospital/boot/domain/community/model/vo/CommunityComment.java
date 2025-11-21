package com.hospital.boot.domain.community.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommunityComment {
	private Long commentId;
    private Long postId;
    private String memberId;
    private String content;
    private String createdAt;
    private String updatedAt; 
}
