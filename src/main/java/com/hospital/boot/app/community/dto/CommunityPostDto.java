package com.hospital.boot.app.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityPostDto {
	private Long postId;
    private String memberId;
    private String category;
    private String title;
    private String content;
    private int views;
    private String createdAt;
    private String updatedAt;
    private String status;
}
