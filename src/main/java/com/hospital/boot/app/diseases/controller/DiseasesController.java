package com.hospital.boot.app.diseases.controller;

import com.hospital.boot.domain.diseases.model.service.DiseasesService;
import com.hospital.boot.domain.diseases.model.vo.Diseases;
import com.hospital.boot.domain.diseases.model.vo.FeaturedDiseases;
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
    public ResponseEntity<Map<String, Object>> detailsOfDisease (
            @RequestParam(value = "name") String name){
        try {
            Map<String, Object> disease = dService.findByName(name);
            if (disease != null) {
                return ResponseEntity.ok(disease);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
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
    public ResponseEntity<List<String>> searchByAi(@RequestBody Map<String, String> request) {
        try {
            String message = request.get("message");

            String symptomPrompt = buildSymptomPrompt(message);
            String symptomsResponse = callGeminiApi(symptomPrompt);
            List<String> symptoms = parseSymptoms(symptomsResponse);

            return ResponseEntity.ok(symptoms);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of());
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

    @GetMapping("/popular")
    public ResponseEntity<Map<String, Object>> getPopularDiseases() {
        try {
            List<Map<String, Object>> popularDiseases = dService.findPopularDiseases();

            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", popularDiseases
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllDiseases() {
        try {
            List<Map<String, Object>> allDiseases = dService.findAllDiseases();

            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", allDiseases
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    // Featured Diseases API
    @GetMapping("/featured")
    public ResponseEntity<List<FeaturedDiseases>> getFeaturedDiseases() {
        try {
            List<FeaturedDiseases> featuredDiseases = dService.findAllFeaturedDiseases();
            return ResponseEntity.ok(featuredDiseases);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/featured")
    public ResponseEntity<Map<String, Object>> addFeaturedDisease(@RequestBody Map<String, String> request) {
        try {
            String diseaseName = request.get("diseaseName");
            if (diseaseName == null || diseaseName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "질병명을 입력해주세요."
                ));
            }

            int result = dService.addFeaturedDisease(diseaseName);
            if (result > 0) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "질병이 추가되었습니다."
                ));
            } else {
                return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "질병 추가에 실패했습니다."
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/featured/{id}")
    public ResponseEntity<Map<String, Object>> removeFeaturedDisease(@PathVariable("id") int featuredDiseasesNo) {
        try {
            int result = dService.removeFeaturedDisease(featuredDiseasesNo);
            if (result > 0) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "질병이 삭제되었습니다."
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "질병을 찾을 수 없습니다."
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/featured/order")
    public ResponseEntity<Map<String, Object>> updateFeaturedDiseasesOrder(@RequestBody List<FeaturedDiseases> list) {
        try {
            int result = dService.updateFeaturedDiseasesOrder(list);
            if (result > 0) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "순서가 변경되었습니다."
                ));
            } else {
                return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "순서 변경에 실패했습니다."
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

}
