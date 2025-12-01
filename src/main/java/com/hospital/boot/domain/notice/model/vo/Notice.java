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
    private int noticeNo; 			// 공지사항 번호
    private String memberId; 		// 회원ID
    private String noticeSubject; 	// 공지사항 제목
    private String noticeContent; 	// 공지사항 내용
    private String noticeWriter; 	// 공지사항 작성자
    private String writeDate; 		// 공지사항 작성일
    private int viewCount; 			// 공지사항 조회수
    private String attachmentName; 	// 첨부파일 이름
    private String attachmentPath; 	// 첨부파일 경로
    private String attachmentDate; 	// 첨부파일 등록일
    private String category; 		// 카테고리
    private String createdAt; 		// 등록일
    private String updatedAt; 		// 수정일
}