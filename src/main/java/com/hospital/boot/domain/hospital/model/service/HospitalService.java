package com.hospital.boot.domain.hospital.model.service;

import com.hospital.boot.domain.hospital.model.vo.Hospital;

import java.util.List;

public interface HospitalService {

    List<Hospital> findHospitalByDepartments(List<String> departments);

    List<Hospital> findEmergencyHospital();

    // 지도 영역 기반 병원 조회
    List<Hospital> findHospitalsByBounds(Double swLat, Double swLng, Double neLat, Double neLng, Boolean emergencyOnly);

    // 관리자용 CRUD
    List<Hospital> getAllHospitals();

    void insertHospital(Hospital hospital);

    void updateHospital(Hospital hospital);

    void deleteHospital(String hospitalId);

    // 병원 진료과 목록 조회
    List<String> getDepartmentsByHospitalId(String hospitalId);

    List<Hospital> findAllHospitals();

    List<Hospital> findByDepartments(List<String> departments);
}
