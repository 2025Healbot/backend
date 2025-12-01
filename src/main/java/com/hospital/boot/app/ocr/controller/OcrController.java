package com.hospital.boot.app.ocr.controller;

import com.hospital.boot.app.ocr.dto.OcrResponse;
import com.hospital.boot.app.ocr.dto.OcrVerifyRequest;
import com.hospital.boot.app.ocr.dto.OcrVerifyResponse;
import com.hospital.boot.app.ocr.service.OcrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
public class OcrController {

    private final OcrService ocrService;

    // ===== 이미지 → 텍스트 =====
    @PostMapping("/receipt")
    public ResponseEntity<?> extractReceiptText(@RequestParam("image") MultipartFile image) {
        try {
            if (image.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "이미지 파일이 비어있습니다.");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "이미지 파일만 업로드 가능합니다.");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            OcrResponse ocrResponse = ocrService.extractTextFromReceipt(image);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", ocrResponse);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("OCR 처리 중 오류 발생", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "OCR 처리 중 오류가 발생했습니다.");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ===== OCR 텍스트로 병원 인증 =====
    @PostMapping("/verifyReceipt")
    public ResponseEntity<OcrVerifyResponse> verifyReceipt(@RequestBody OcrVerifyRequest request) {

        OcrVerifyResponse result = ocrService.verifyReceipt(
                request.getHospitalId(),
                request.getOcrText()
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "OCR 서비스가 정상 작동 중입니다.");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}
