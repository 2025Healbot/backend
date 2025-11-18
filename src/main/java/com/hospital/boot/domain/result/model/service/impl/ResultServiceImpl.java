package com.hospital.boot.domain.result.model.service.impl;

import com.hospital.boot.domain.result.model.service.ResultService;
import com.hospital.boot.domain.result.model.mapper.ResultMapper;
import com.hospital.boot.domain.hospital.model.vo.Hospital;
import com.hospital.boot.domain.diseases.model.vo.Diseases;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final ResultMapper rMapper;

    @Override
    public Map<String, Object> searchAll(String keyword) {
        // 1. 증상 정보 조회 (정확 일치)
        String symptomInfo = rMapper.getSymptomInfo(keyword);

        // 2. 병원 검색 (LIKE, 이름순 정렬)
        List<Hospital> hospitals = rMapper.searchHospitalsByName(keyword);

        // 3. 질병 검색 (정확도순: 이름 일치 우선, 설명 일치)
        List<Diseases> diseases = rMapper.searchDiseasesByNameOrDescription(keyword);

        // 4. 결과 구성
        Map<String, Object> results = new HashMap<>();
        results.put("hospitals", hospitals);
        results.put("diseases", diseases);
        results.put("notices", null);      // 추후 구현
        results.put("communities", null);  // 추후 구현

        // 5. 최종 응답
        Map<String, Object> response = new HashMap<>();
        response.put("keyword", keyword);
        response.put("symptomInfo", symptomInfo);  // null이면 증상 아님
        response.put("results", results);

        return response;
    }
}
