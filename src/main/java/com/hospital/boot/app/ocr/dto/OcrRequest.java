package com.hospital.boot.app.ocr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 네이버 클로버 OCR API 요청 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrRequest {
    private String version;      // API 버전 (V2)
    private String requestId;    // 요청 고유 ID
    private Long timestamp;      // 타임스탬프
    private List<OcrImageInfo> images;  // 이미지 정보 목록
}
