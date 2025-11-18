package com.hospital.boot.domain.symptom.model.service.impl;

import com.hospital.boot.domain.symptom.model.service.SymptomService;
import com.hospital.boot.domain.symptom.model.mapper.SymptomMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SymptomServiceImpl implements SymptomService {

    private final SymptomMapper sMapper;

    @Override
    public String symptomDetails(String symptom) {
        return sMapper.symptomDetails(symptom);
    }
}
