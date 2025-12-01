package com.hospital.boot.domain.accesslog.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AccessLog {
    private int logId; 			// 로그ID
    private String memberId; 	// 회원ID
    private String accessDate; 	// 접속일
    private String ipAddress; 	// 클라이언트 주소
    private String userAgent;	// 브라우저 정보
}