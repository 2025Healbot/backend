package com.hospital.boot.domain.community.model.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommunityComment {

    private Long commentId;   // COMMENT_ID
    private Long postId;      // POST_ID
    private String memberId;  // MEMBER_ID
    private String content;   // CONTENT
    private Date createdAt;   // CREATED_AT
    private Date updatedAt;   // UPDATED_AT
}