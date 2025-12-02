package com.hospital.boot.domain.hospital.model.service;

import com.hospital.boot.domain.hospital.model.vo.Hospital;

import java.util.List;

public interface HospitalService {

    List<Hospital> findHospitalByDepartments(List<String> departments);

    List<Hospital> findEmergencyHospital();

    // 지도 영역 기반 병원 조회
    List<Hospital> findHospitalsByBounds(Double swLat, Double swLng, Double neLat, Double neLng, Boolean emergencyOnly);

    // 관리자 병원 목록 조회 (AdminController)
    List<Hospital> getAllHospitals();

    // 관리자 병원 등록 (AdminController)
    void insertHospital(Hospital hospital);

    // 관리자 병원 수정 (AdminController)
    void updateHospital(Hospital hospital);

    // 관리자 병원 삭제 (AdminController)
    void deleteHospital(String hospitalId);

    // 관리자 병원 진료과 목록 조회 (AdminController)
    List<String> getDepartmentsByHospitalId(String hospitalId);

    // 병원 진료과 추가
    int insertHospitalDepartment(String hospitalId, String department);

    // 병원 진료과 삭제
    int deleteHospitalDepartments(String hospitalId);

    List<Hospital> findAllHospitals();

    List<Hospital> findByDepartments(List<String> departments);
}