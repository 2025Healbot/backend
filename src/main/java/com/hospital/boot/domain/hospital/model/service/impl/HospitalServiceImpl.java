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

    // 관리자용 CRUD 구현
    @Override
    public List<Hospital> getAllHospitals() {
        return hMapper.getAllHospitals();
    }

    @Override
    public void insertHospital(Hospital hospital) {
        hMapper.insertHospital(hospital);
    }

    @Override
    public void updateHospital(Hospital hospital) {
        hMapper.updateHospital(hospital);
    }

    @Override
    public void deleteHospital(String hospitalId) {
        hMapper.deleteHospital(hospitalId);
    }

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
}
