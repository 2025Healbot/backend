package com.hospital.boot.domain.hospital.model.service.impl;

import com.hospital.boot.domain.hospital.model.vo.Hospital;
import com.hospital.boot.domain.hospital.model.service.HospitalService;
import com.hospital.boot.domain.hospital.model.mapper.HospitalMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalMapper hMapper;

    @Override
    public List<Hospital> findHospitalByDepartments(List<String> departments) {
        return hMapper.findHospitalByDepartments(departments);
    }

    @Override
    public List<Hospital> findEmergencyHospital() {
        return hMapper.findEmergencyHospital();
    }

    @Override
    public List<Hospital> findHospitalsByBounds(Double swLat, Double swLng, Double neLat, Double neLng, Boolean emergencyOnly) {
        return hMapper.findHospitalsByBounds(swLat, swLng, neLat, neLng, emergencyOnly);
    }

    // 관리자 병원 목록 조회 (HospitalService)
    @Override
    public List<Hospital> getAllHospitals() {
        return hMapper.getAllHospitals();
    }

    // 관리자 병원 등록 (HostpitalService)
    @Override
    public void insertHospital(Hospital hospital) {
        hMapper.insertHospital(hospital);
    }

    // 관리자 병원 수정 (HospitalService)
    @Override
    public void updateHospital(Hospital hospital) {
        hMapper.updateHospital(hospital);
    }

    // 관리자 병원 삭제 (HospitalService)
    @Override
    public void deleteHospital(String hospitalId) {
        hMapper.deleteHospital(hospitalId);
    }

    // 관리자 병원 진료과 목록 조회 (HospitalService)
    @Override
    public List<String> getDepartmentsByHospitalId(String hospitalId) {
        return hMapper.getDepartmentsByHospitalId(hospitalId);
    }

    @Override
    public List<Hospital> findAllHospitals() {
        return hMapper.getAllHospitals();
    }

    @Override
    public List<Hospital> findByDepartments(List<String> departments) {
        return hMapper.findHospitalByDepartments(departments);
    }

    // 병원 진료과 추가
    @Override
    public int insertHospitalDepartment(String hospitalId, String department) {
        return hMapper.insertHospitalDepartment(hospitalId, department);
    }

    // 병원 진료과 삭제
    @Override
    public int deleteHospitalDepartments(String hospitalId) {
        return hMapper.deleteHospitalDepartments(hospitalId);
    }
}