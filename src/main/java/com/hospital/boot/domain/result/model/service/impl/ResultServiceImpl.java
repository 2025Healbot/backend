package com.hospital.boot.domain.result.model.service.impl;

import com.hospital.boot.domain.result.model.service.ResultService;
import com.hospital.boot.domain.result.model.mapper.ResultMapper;
import com.hospital.boot.domain.hospital.model.vo.Hospital;
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
        String symptomInfo = null;
        List<String> matchedSymptoms = new java.util.ArrayList<>();

        // 1. DB에서 정확 일치하는 증상만 찾기 (AI 제거)
        String exactMatchInfo = rMapper.getSymptomInfo(keyword);
        if (exactMatchInfo != null) {
            matchedSymptoms.add(keyword);
            symptomInfo = keyword + ": " + exactMatchInfo;
        }

        // 2. 병원 검색 (LIKE, 이름순 정렬)
        List<Hospital> hospitals = rMapper.searchHospitalsByName(keyword);

        // 3. 질병 검색 (정확도순: 이름 일치 우선, 설명 일치) - Map으로 반환 (진료과, 증상 포함)
        List<Map<String, Object>> diseases = rMapper.searchDiseasesByNameOrDescription(keyword);

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
        response.put("matchedSymptoms", matchedSymptoms);  // 매칭된 증상 목록
        response.put("results", results);

        return response;
    }
}
