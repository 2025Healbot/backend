package com.hospital.boot.app.ocr.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.boot.app.ocr.dto.HospitalOcrInfo;
import com.hospital.boot.app.ocr.dto.OcrRequest;
import com.hospital.boot.app.ocr.dto.OcrResponse;
import com.hospital.boot.app.ocr.dto.OcrVerifyResponse;
import com.hospital.boot.app.ocr.dto.OcrImageInfo;
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
                .addFormDataPart(
                        "file",
                        originalFilename,
                        RequestBody.create(
                                imageFile.getBytes(),
                                MediaType.parse(imageFile.getContentType())
                        )
                )
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

    // ===== 2) OCR 텍스트만으로 병원 찾기 =====
    public OcrVerifyResponse verifyReceiptByText(String ocrText) {

        if (ocrText == null || ocrText.trim().isEmpty()) {
            return new OcrVerifyResponse(false, false,
                    "추출된 텍스트가 비어 있습니다.", null, null);
        }

        // 1) 병원명 들어있을 법한 줄 먼저 찾기
        String[] lines = ocrText.split("\\r?\\n");
        String hospitalLine = null;

        for (String line : lines) {
            if (line == null) continue;
            String t = line.trim();
            if (t.isEmpty()) continue;

            // 병원 관련 키워드
            if (t.contains("병원") || t.contains("의원") ||
                t.contains("치과") || t.contains("한의원")) {
                hospitalLine = t;
                break;
            }
        }

        if (hospitalLine == null) {
            return new OcrVerifyResponse(false, false,
                    "영수증에서 병원명을 찾지 못했습니다.", null, null);
        }

        // 2) 특수문자 제거 후 키워드 추출
        String keyword = hospitalLine.replaceAll("[^0-9a-zA-Z가-힣]", "");
        log.info("[OCR VERIFY] hospitalLine={}, keyword={}", hospitalLine, keyword);

        // 3) DB에서 병원명으로 병원 여러 개 찾기
        java.util.List<HospitalOcrInfo> candidates =
                ocrMapper.searchHospitalByNameForOCR(keyword);

        if (candidates == null || candidates.isEmpty()) {
            return new OcrVerifyResponse(false, false,
                    "해당 병원을 찾을 수 없습니다.", null, null);
        }

        // 4) 정규화 텍스트 준비
        String normText = normalize(ocrText);

        HospitalOcrInfo best = null;
        int bestScore = -1;

        for (HospitalOcrInfo info : candidates) {
            if (info == null) continue;

            String rawName = info.getHospitalName() != null ? info.getHospitalName() : "";
            String mainName = rawName.split("[\\(\\[]")[0]; // "푸른치과의원(노원점)" -> "푸른치과의원"
            String normName = normalize(mainName);
            String normAddrFull = normalize(info.getAddress());

            boolean nameMatch = !normName.isEmpty() && normText.contains(normName);
            int addrScore = calcAddressMatchScore(info.getAddress(), normText);

            // 점수: 이름매칭되면 +10, 주소 토큰 매칭 수를 더함
            int score = (nameMatch ? 10 : 0) + addrScore;

            log.info("[OCR VERIFY] candidate id={}, name={}, addr={}, nameMatch={}, addrScore={}, totalScore={}",
                    info.getHospitalId(), rawName, info.getAddress(), nameMatch, addrScore, score);

            if (score > bestScore) {
                bestScore = score;
                best = info;
            }
        }

        // 5) 최종 판단
        if (best == null || bestScore <= 0) {
            return new OcrVerifyResponse(
                    true,
                    false,
                    "영수증의 병원명/주소가 병원 정보와 충분히 일치하지 않습니다.",
                    null,
                    null
            );
        }

        return new OcrVerifyResponse(
                true,
                true,
                "영수증 인증이 완료되었습니다.",
                best.getHospitalId(),
                best.getHospitalName()
        );
    }

    // ===== 헬퍼들 =====

    /**
     * 한글/영문/숫자만 남기고 소문자 + 공백/특수문자 제거
     */
    private String normalize(String s) {
        if (s == null) return "";
        return s.toLowerCase()
                .replaceAll("\\s+", "")
                .replaceAll("[^0-9a-z가-힣]", "");
    }

    /**
     * 주소를 공백/쉼표/괄호 등으로 나눠서,
     * 길이 2 이상 토큰이 OCR 텍스트에 몇 개나 포함되는지 계산
     */
    private int calcAddressMatchScore(String address, String normText) {
        if (address == null || address.isEmpty()) return 0;

        String[] rawTokens = address.split("[,()\\-_/\\s]+");
        int score = 0;

        for (String token : rawTokens) {
            if (token == null) continue;
            token = token.trim();
            if (token.length() < 2) continue; // "동", "호" 같은 건 버림

            String normToken = normalize(token);
            if (!normToken.isEmpty() && normText.contains(normToken)) {
                score++;
            }
        }
        return score;
    }
}
