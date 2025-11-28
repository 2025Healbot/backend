package com.hospital.boot.app.ocr.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.boot.app.ocr.dto.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 네이버 클로버 OCR API 서비스
 */
@Slf4j
@Service
public class OcrService {

    @Value("${naver.ocr.api.url}")
    private String ocrApiUrl;

    @Value("${naver.ocr.secret.key}")
    private String secretKey;

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OcrService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 영수증 이미지에서 텍스트 추출
     * @param imageFile 업로드된 이미지 파일
     * @return OCR 결과
     * @throws IOException
     */
    public OcrResponse extractTextFromReceipt(MultipartFile imageFile) throws IOException {
        log.info("OCR 처리 시작 - 파일명: {}, 크기: {} bytes",
                imageFile.getOriginalFilename(), imageFile.getSize());

        // 파일 확장자 추출
        String originalFilename = imageFile.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);

        // OCR API 요청 메시지 구성
        OcrRequest ocrRequest = new OcrRequest(
                "V2",
                String.valueOf(System.currentTimeMillis()),
                System.currentTimeMillis(),
                Collections.singletonList(new OcrImageInfo(fileExtension, originalFilename))
        );

        String messageJson = objectMapper.writeValueAsString(ocrRequest);
        log.debug("OCR 요청 메시지: {}", messageJson);

        // Multipart 요청 구성
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("message", messageJson)
                .addFormDataPart("file", originalFilename,
                        RequestBody.create(imageFile.getBytes(),
                                MediaType.parse(imageFile.getContentType())))
                .build();

        // HTTP 요청 생성
        Request request = new Request.Builder()
                .url(ocrApiUrl)
                .addHeader("X-OCR-SECRET", secretKey)
                .post(requestBody)
                .build();

        // API 호출
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                log.error("OCR API 호출 실패 - 상태코드: {}, 응답: {}", response.code(), errorBody);
                throw new IOException("OCR API 호출 실패: " + response.code() + " - " + errorBody);
            }

            String responseBody = response.body().string();
            log.debug("OCR API 응답: {}", responseBody);

            OcrResponse ocrResponse = objectMapper.readValue(responseBody, OcrResponse.class);
            log.info("OCR 처리 완료 - 추출된 이미지 수: {}",
                    ocrResponse.getImages() != null ? ocrResponse.getImages().size() : 0);

            return ocrResponse;
        }
    }

    /**
     * 파일 확장자 추출
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "jpg";
        }
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "jpg";
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }
}
