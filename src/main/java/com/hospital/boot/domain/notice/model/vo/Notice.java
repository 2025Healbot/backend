package com.hospital.boot.domain.notice.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Notice {
    private int noticeId;
    private String title;
    private String content;
    private String category;
    private int views;
    private String createdAt;
    private String updatedAt;
}
