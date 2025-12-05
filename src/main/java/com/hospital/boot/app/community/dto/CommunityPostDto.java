package com.hospital.boot.app.community.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class CommunityPostDto {
    private Long postId;
    private String memberId;
    private String userName;
    private String category;
    private String title;
    private String content;
    private Integer views;
    private Integer commentCount;  // 댓글 수
    private Date createdAt;
    private Date updatedAt;
}