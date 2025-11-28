package com.hospital.boot.app.ocr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 네이버 클로버 OCR API 응답 DTO
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OcrResponse {
    private String version;        // API 버전
    private String requestId;      // 요청 ID
    private Long timestamp;        // 타임스탬프
    private List<OcrImage> images; // 이미지 결과 목록
}
