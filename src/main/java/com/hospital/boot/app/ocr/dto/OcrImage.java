package com.hospital.boot.app.ocr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * OCR 이미지 결과 DTO
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OcrImage {
    private String uid;            // 이미지 고유 ID
    private String name;           // 이미지 이름
    private String inferResult;    // 추론 결과 (SUCCESS, FAILURE)
    private String message;        // 메시지
    private List<OcrField> fields; // 추출된 필드 목록
}
