package com.hospital.boot.app.diseases.controller;

import com.hospital.boot.domain.diseases.model.service.DiseasesService;
import com.hospital.boot.domain.diseases.model.vo.Diseases;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/diseases")
@RequiredArgsConstructor
public class DiseasesController {

    private final DiseasesService dService;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @GetMapping("")
    public List<Diseases> detailsOfDisease (
            @RequestParam(value = "name") String name){
        return dService.findByName(name);
    }

    @PostMapping("/search")
    public String searchDiseases(@RequestBody Map<String, List<String>> request) {
        try {
            List<String> symptoms = request.get("symptoms");

            if (symptoms != null && symptoms.size() == 1 && symptoms.get(0).contains(",")) {
                symptoms = parseSymptoms(symptoms.get(0));
            }

            return dService.findDiseasesBySymptoms(symptoms);
        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @PostMapping("/search/ai")
    public String searchByAi(@RequestBody Map<String, String> request) {
        try {
            String message = request.get("message");

            String symptomPrompt = buildSymptomPrompt(message);
            String symptomsResponse = callGeminiApi(symptomPrompt);
            List<String> symptoms = parseSymptoms(symptomsResponse);

            return dService.findDiseasesBySymptoms(symptoms);
        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    private String callGeminiApi(String prompt) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", geminiApiKey);

        Map<String, Object> requestBody = Map.of("contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map<String, Object>> response = new RestTemplate().postForEntity(geminiApiUrl, entity, (Class<Map<String, Object>>) (Class<?>) Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            @SuppressWarnings("unchecked") List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                @SuppressWarnings("unchecked") Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                @SuppressWarnings("unchecked") List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    return (String) parts.get(0).get("text");
                }
            }
        }
        throw new Exception("Gemini API 응답을 받지 못했습니다");
    }

    private String buildSymptomPrompt(String message) {
        List<String> symptoms = dService.allSymptoms();
        String symptomsStr = String.join(", ", symptoms);
        return "다음 증상 목록에서만 키워드를 찾아서 쉼표로 구분해서 출력하세요. 설명이나 제목 없이 키워드만 출력하세요.\n\n" + "증상 목록: " + symptomsStr + "\n\n" + "사용자 입력: " + message + "\n\n" + "출력 형식: 증상1, 증상2, 증상3 (예: 두통, 복통, 발열)\n" + "중요: 반드시 증상 목록에 있는 것만 선택하고, 쉼표로 구분하여 출력하세요.\n" + "답변:";
    }

    private List<String> parseSymptoms(String response) {
        return List.of(response.trim().split("\\s*,\\s*"));
    }

}
