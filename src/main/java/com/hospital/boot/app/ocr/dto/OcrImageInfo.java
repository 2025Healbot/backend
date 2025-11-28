package com.hospital.boot.app.ocr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OCR 이미지 정보 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrImageInfo {
    private String format;  // 이미지 포맷 (jpg, png 등)
    private String name;    // 이미지 파일명
}
