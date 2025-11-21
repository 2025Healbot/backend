package com.hospital.boot.app.community.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityCommentDto {

    private Long commentId;
    private Long postId;
    private String memberId;
    private String content;
    private String createdAt;
    private String updatedAt;
}