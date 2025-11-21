package com.hospital.boot.app.community.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class CommunityPostWriteRequest {
    private String category;
    private String title;
    private String content;
    private String tags;   // 지금은 DB에 안 쓰고 그냥 받아만 둠
}