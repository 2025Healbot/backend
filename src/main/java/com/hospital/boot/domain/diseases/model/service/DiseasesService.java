package com.hospital.boot.domain.diseases.model.service;

import com.hospital.boot.domain.diseases.model.vo.Diseases;
import com.hospital.boot.domain.diseases.model.vo.FeaturedDiseases;

import java.util.List;
import java.util.Map;

public interface DiseasesService {

    Map<String, Object> findByName(String diseaseName);

    List<String> allSymptoms();

    String findDiseasesBySymptoms(List<String> symptoms);

    List<Map<String, Object>> findPopularDiseases();

    List<Map<String, Object>> findAllDiseases();

    // Featured Diseases
    List<FeaturedDiseases> findAllFeaturedDiseases();

    int addFeaturedDisease(String diseaseName);

    int removeFeaturedDisease(int featuredDiseasesNo);

    int updateFeaturedDiseasesOrder(List<FeaturedDiseases> list);

    // Disease CRUD
    int addDisease(Diseases disease);

    int updateDisease(Diseases disease);

    int deleteDisease(int diseaseNo);

    Diseases findByDiseaseNo(int diseaseNo);

    // Disease Departments & Symptoms
    int insertDiseaseDepartment(String diseaseName, String departmentName);

    int deleteDiseaseDepartments(String diseaseName);

    int insertDiseaseSymptom(String diseaseName, String symptomName);

    int deleteDiseaseSymptoms(String diseaseName);
}
