package com.hospital.boot.domain.diseases.model.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.boot.domain.diseases.model.service.DiseasesService;
import com.hospital.boot.domain.diseases.model.mapper.DiseasesMapper;
import com.hospital.boot.domain.diseases.model.vo.Diseases;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiseasesServiceImpl implements DiseasesService {

    private final DiseasesMapper dMapper;
    private final ObjectMapper objectMapper;

    @Override
    public List<Diseases> findByName(String diseaseName) {
        return dMapper.findByName(diseaseName);
    }

    @Override
    public List<String> allSymptoms() {
        return dMapper.allSymptoms();
    }

    @Override
    public String findDiseasesBySymptoms(List<String> symptoms) {
        try {
            List<Map<String, Object>> diseases = dMapper.findDiseasesBySymptoms(symptoms);

            Map<String, Object> response = Map.of(
                "matched", String.join(", ", symptoms),
                "data", diseases
            );

            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
}
