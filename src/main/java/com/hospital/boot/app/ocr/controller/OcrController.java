package com.hospital.boot.app.ocr.controller;

import com.hospital.boot.app.ocr.dto.OcrResponse;
import com.hospital.boot.app.ocr.service.OcrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * OCR API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
public class OcrController {

    private final OcrService ocrService;

    /**
     * 영수증 이미지에서 텍스트 추출
     * @param image 업로드된 이미지 파일
     * @return OCR 추출 결과
     */
    @PostMapping("/receipt")
    public ResponseEntity<?> extractReceiptText(@RequestParam("image") MultipartFile image) {
        try {
            log.info("영수증 OCR 요청 - 파일명: {}, 크기: {} bytes",
                    image.getOriginalFilename(), image.getSize());

            // 파일 검증
            if (image.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "이미지 파일이 비어있습니다.");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 파일 타입 검증
            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "이미지 파일만 업로드 가능합니다.");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // OCR 처리
            OcrResponse ocrResponse = ocrService.extractTextFromReceipt(image);

            // 성공 응답
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", ocrResponse);

            log.info("영수증 OCR 처리 완료");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("OCR 처리 중 오류 발생", e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "OCR 처리 중 오류가 발생했습니다.");
            errorResponse.put("details", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    /**
     * OCR 서비스 상태 확인
     * @return 서비스 상태
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "OCR 서비스가 정상 작동 중입니다.");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}
