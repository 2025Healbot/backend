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

    private Long commentId;   // 커뮤니티 댓글ID
    private Long postId;      // 커뮤니티 게시글ID
    private String memberId;  // 회원ID
    private String content;   // 댓글 내용
    private Date createdAt;   // 등록일
    private Date updatedAt;   // 수정일
}