package com.hospital.boot.domain.diseases.model.service;

import com.hospital.boot.domain.diseases.model.vo.Diseases;

import java.util.List;
import java.util.Map;

public interface DiseasesService {

    Map<String, Object> findByName(String diseaseName);

    List<String> allSymptoms();

    String findDiseasesBySymptoms(List<String> symptoms);

    List<Map<String, Object>> findPopularDiseases();
}
