package com.hospital.boot.domain.diseases.model.service;

import com.hospital.boot.domain.diseases.model.vo.Diseases;

import java.util.List;

public interface DiseasesService {

    List<Diseases> findByName(String diseaseName);

    List<String> allSymptoms();

    String findDiseasesBySymptoms(List<String> symptoms);
}
