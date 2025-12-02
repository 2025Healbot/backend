package com.hospital.boot.domain.diseases.model.mapper;

import com.hospital.boot.domain.diseases.model.vo.Diseases;
import com.hospital.boot.domain.diseases.model.vo.FeaturedDiseases;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DiseasesMapper {

    Map<String, Object> findByName(String diseaseName);

    List<String> allSymptoms();

    List<Map<String, Object>> findDiseasesBySymptoms(@Param("symptoms") List<String> symptoms);

    List<Map<String, Object>> findPopularDiseases();

    List<Map<String, Object>> findAllDiseases();

    // Featured Diseases
    List<FeaturedDiseases> findAllFeaturedDiseases();

    int insertFeaturedDisease(FeaturedDiseases featuredDisease);

    int deleteFeaturedDisease(int featuredDiseasesNo);

    int updateFeaturedDiseaseOrder(@Param("item") FeaturedDiseases item);

    // Disease CRUD
    int insertDisease(Diseases disease);

    int updateDisease(Diseases disease);

    int deleteDisease(int diseaseNo);

    Diseases findByDiseaseNo(int diseaseNo);

    // Disease Departments
    int insertDiseaseDepartment(@Param("diseaseName") String diseaseName, @Param("departmentName") String departmentName);

    int deleteDiseaseDepartments(@Param("diseaseName") String diseaseName);

    // Disease Symptoms
    int insertDiseaseSymptom(@Param("diseaseName") String diseaseName, @Param("symptomName") String symptomName);

    int deleteDiseaseSymptoms(@Param("diseaseName") String diseaseName);
}
