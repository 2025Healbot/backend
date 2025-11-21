package com.hospital.boot.app.community.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class CommunityCommentWriteRequest {

    private String content;
}
