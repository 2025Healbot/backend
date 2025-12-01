package com.hospital.boot.app.ocr.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OcrVerifyRequest {
	private String hospitalId; //OCR에서 추출된 전체 택스트
	private String ocrText;
}
