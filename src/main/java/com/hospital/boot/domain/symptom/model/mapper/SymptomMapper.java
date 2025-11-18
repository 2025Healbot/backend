package com.hospital.boot.domain.symptom.model.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SymptomMapper {

    String symptomDetails(String symptom);
}
