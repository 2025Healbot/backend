package com.hospital.boot.app.ocr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OcrVerifyResponse {
	private boolean success;       // API 호출 성공 여부
    private boolean verified;      // 인증 여부
    private String message;        // 메시지
    private String hospitalId;
    private String hospitalName;
}
