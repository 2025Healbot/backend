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
    private String author;     // 화면에 표시할 작성자 이름(회원 이름)
    private String category;
    private String title;
    private String content;
    private Integer views;
    private Date createdAt;
    private Date updatedAt;
}