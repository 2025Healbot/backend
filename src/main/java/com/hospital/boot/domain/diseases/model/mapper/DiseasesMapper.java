package com.hospital.boot.domain.diseases.model.mapper;

import com.hospital.boot.domain.diseases.model.vo.Diseases;
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
}
