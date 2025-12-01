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
}
