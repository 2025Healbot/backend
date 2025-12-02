package com.hospital.boot.domain.diseases.model.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.boot.domain.diseases.model.service.DiseasesService;
import com.hospital.boot.domain.diseases.model.mapper.DiseasesMapper;
import com.hospital.boot.domain.diseases.model.vo.Diseases;
import com.hospital.boot.domain.diseases.model.vo.FeaturedDiseases;
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
    public Map<String, Object> findByName(String diseaseName) {
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

    @Override
    public List<Map<String, Object>> findPopularDiseases() {
        return dMapper.findPopularDiseases();
    }

    @Override
    public List<Map<String, Object>> findAllDiseases() {
        return dMapper.findAllDiseases();
    }

    @Override
    public List<FeaturedDiseases> findAllFeaturedDiseases() {
        return dMapper.findAllFeaturedDiseases();
    }

    @Override
    public int addFeaturedDisease(String diseaseName) {
        // 현재 최대 순서 번호 가져오기
        List<FeaturedDiseases> currentList = dMapper.findAllFeaturedDiseases();
        int maxOrder = currentList.isEmpty() ? 0 : currentList.stream()
                .mapToInt(FeaturedDiseases::getDisplayOrder)
                .max()
                .orElse(0);

        FeaturedDiseases featuredDisease = new FeaturedDiseases();
        featuredDisease.setDiseaseName(diseaseName);
        featuredDisease.setDisplayOrder(maxOrder + 1);

        return dMapper.insertFeaturedDisease(featuredDisease);
    }

    @Override
    public int removeFeaturedDisease(int featuredDiseasesNo) {
        return dMapper.deleteFeaturedDisease(featuredDiseasesNo);
    }

    @Override
    public int updateFeaturedDiseasesOrder(List<FeaturedDiseases> list) {
        int totalUpdated = 0;
        for (FeaturedDiseases item : list) {
            totalUpdated += dMapper.updateFeaturedDiseaseOrder(item);
        }
        return totalUpdated;
    }

    @Override
    public int addDisease(Diseases disease) {
        return dMapper.insertDisease(disease);
    }

    @Override
    public int updateDisease(Diseases disease) {
        return dMapper.updateDisease(disease);
    }

    @Override
    public int deleteDisease(int diseaseNo) {
        return dMapper.deleteDisease(diseaseNo);
    }

    @Override
    public Diseases findByDiseaseNo(int diseaseNo) {
        return dMapper.findByDiseaseNo(diseaseNo);
    }

    @Override
    public int insertDiseaseDepartment(String diseaseName, String departmentName) {
        return dMapper.insertDiseaseDepartment(diseaseName, departmentName);
    }

    @Override
    public int deleteDiseaseDepartments(String diseaseName) {
        return dMapper.deleteDiseaseDepartments(diseaseName);
    }

    @Override
    public int insertDiseaseSymptom(String diseaseName, String symptomName) {
        return dMapper.insertDiseaseSymptom(diseaseName, symptomName);
    }

    @Override
    public int deleteDiseaseSymptoms(String diseaseName) {
        return dMapper.deleteDiseaseSymptoms(diseaseName);
    }
}
