package com.hospital.boot.domain.hospital.model.service;

import com.hospital.boot.app.hospital.dto.HospitalSearchRequest;
import com.hospital.boot.app.hospital.dto.HospitalWithDistance;
import com.hospital.boot.domain.hospital.model.vo.Hospital;

import java.util.List;

public interface HospitalService {

    List<Hospital> findHospitalByDepartments(List<String> departments);

    List<HospitalWithDistance> findHospitalByDepartmentsWithDistance(HospitalSearchRequest request);

    List<Hospital> findEmergencyHospital();

    // 관리자용 CRUD
    List<Hospital> getAllHospitals();

    void insertHospital(Hospital hospital);

    void updateHospital(Hospital hospital);

    void deleteHospital(String hospitalId);

    // 병원 진료과 목록 조회
    List<String> getDepartmentsByHospitalId(String hospitalId);
}
