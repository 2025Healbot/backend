package com.hospital.boot.app.result.controller;

import com.hospital.boot.domain.result.model.service.ResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/api/result")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService rService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> search(@RequestParam("keyword") String keyword) {
        try {
            Map<String, Object> result = rService.searchAll(keyword);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "검색 중 오류가 발생했습니다.",
                "message", e.getMessage()
            ));
        }
    }
}
