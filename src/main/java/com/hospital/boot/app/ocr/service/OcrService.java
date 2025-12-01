package com.hospital.boot.app.ocr.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.boot.app.ocr.dto.*;
import com.hospital.boot.app.ocr.mapper.OCRMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OcrService {

    private final OCRMapper ocrMapper;

    @Value("${naver.ocr.api.url}")
    private String ocrApiUrl;

    @Value("${naver.ocr.secret.key}")
    private String secretKey;

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ===== 1) 이미지 → 텍스트 =====
    public OcrResponse extractTextFromReceipt(MultipartFile imageFile) throws IOException {
        log.info("OCR 처리 시작 - 파일명: {}, 크기: {} bytes",
                imageFile.getOriginalFilename(), imageFile.getSize());

        String originalFilename = imageFile.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);

        OcrRequest ocrRequest = new OcrRequest(
                "V2",
                String.valueOf(System.currentTimeMillis()),
                System.currentTimeMillis(),
                Collections.singletonList(new OcrImageInfo(fileExtension, originalFilename))
        );

        String messageJson = objectMapper.writeValueAsString(ocrRequest);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("message", messageJson)
                .addFormDataPart("file", originalFilename,
                        RequestBody.create(imageFile.getBytes(),
                                MediaType.parse(imageFile.getContentType())))
                .build();

        Request request = new Request.Builder()
                .url(ocrApiUrl)
                .addHeader("X-OCR-SECRET", secretKey)
                .post(requestBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                throw new IOException("OCR API 호출 실패: " + response.code() + " - " + errorBody);
            }

            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, OcrResponse.class);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) return "jpg";
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) return "jpg";
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    // ===== 2) 병원 인증 =====
    public OcrVerifyResponse verifyReceipt(String hospitalId, String ocrText) {

        if (hospitalId == null || hospitalId.trim().isEmpty()) {
            return new OcrVerifyResponse(false, false,
                    "병원 ID가 없습니다.", null, null);
        }
        if (ocrText == null || ocrText.trim().isEmpty()) {
            return new OcrVerifyResponse(false, false,
                    "추출된 텍스트가 비어 있습니다.", null, null);
        }

        HospitalOcrInfo info = ocrMapper.findHospitalForOCR(hospitalId);
        if (info == null) {
            return new OcrVerifyResponse(false, false,
                    "해당 병원을 찾을 수 없습니다.", null, null);
        }

        // 공백/줄바꿈 제거 후 비교
        String normalizedText   = ocrText.replaceAll("\\s+", "");
        String nameNorm         = info.getHospitalName().replaceAll("\\s+", "");
        String addrNorm         = info.getAddress() != null
                ? info.getAddress().replaceAll("\\s+", "")
                : "";

        boolean nameMatch = normalizedText.contains(nameNorm);
        boolean addrMatch = addrNorm.isEmpty() || normalizedText.contains(addrNorm);

        if (nameMatch && addrMatch) {
            return new OcrVerifyResponse(true, true,
                    "영수증 인증이 완료되었습니다.",
                    info.getHospitalId(), info.getHospitalName());
        } else {
            return new OcrVerifyResponse(true, false,
                    "영수증의 병원명/주소가 병원 정보와 일치하지 않습니다.",
                    info.getHospitalId(), info.getHospitalName());
        }
    }
}
