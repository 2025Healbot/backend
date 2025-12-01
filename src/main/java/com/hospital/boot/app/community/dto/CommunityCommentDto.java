package com.hospital.boot.app.community.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommunityCommentDto {
    private Long commentId;
    private Long postId;
    private String memberId;
    private String content;
    private String createdAt;   // TO_CHAR
}