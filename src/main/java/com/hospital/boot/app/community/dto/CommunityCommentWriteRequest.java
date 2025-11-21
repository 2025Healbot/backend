package com.hospital.boot.app.community.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommunityCommentWriteRequest {
    private String content;
}