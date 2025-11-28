package com.hospital.boot.app.ocr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OCR 추출된 필드 정보
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OcrField {
    private String valueType;      // 값 타입
    private String inferText;      // 추출된 텍스트
    private Double inferConfidence; // 신뢰도
}
