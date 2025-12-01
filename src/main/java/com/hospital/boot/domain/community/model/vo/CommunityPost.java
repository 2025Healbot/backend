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
public class CommunityPost {

    private Long postId;      // 커뮤니티 게시글ID
    private String memberId;  // 회원ID
    private String category;  // 카테고리
    private String title;     // 커뮤니티 제목
    private String content;   // 커뮤니티 내용
    private Integer views;    // 커뮤니티 조회수
    private Date createdAt;   // 등록일
    private Date updatedAt;   // 수정일
    private String status;    // 커뮤니티 상태
}
