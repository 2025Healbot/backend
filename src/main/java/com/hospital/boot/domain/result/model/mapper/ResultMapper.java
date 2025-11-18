package com.hospital.boot.domain.result.model.mapper;

import com.hospital.boot.domain.hospital.model.vo.Hospital;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ResultMapper {

    // 증상 정보 조회 (정확 일치)
    String getSymptomInfo(@Param("keyword") String keyword);

    // 병원 이름으로 검색 (LIKE, 이름순 정렬)
    List<Hospital> searchHospitalsByName(@Param("keyword") String keyword);

    // 질병 검색 (정확도순: 이름 일치 우선, 그 다음 설명 일치) - Map으로 반환 (진료과, 증상 포함)
    List<Map<String, Object>> searchDiseasesByNameOrDescription(@Param("keyword") String keyword);
}
