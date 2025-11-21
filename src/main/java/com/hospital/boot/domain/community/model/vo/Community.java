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
public class Community {

    private Long postId;      // POST_ID
    private String memberId;  // MEMBER_ID
    private String category;  // CATEGORY
    private String title;     // TITLE
    private String content;   // CONTENT
    private Integer views;    // VIEWS
    private Date createdAt;   // CREATED_AT
    private Date updatedAt;   // UPDATED_AT
    private String status;    // STATUS
}


