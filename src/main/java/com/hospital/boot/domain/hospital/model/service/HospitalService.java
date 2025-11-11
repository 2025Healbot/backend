package com.hospital.boot.domain.hospital.model.service;

import com.hospital.boot.app.hospital.dto.HospitalSearchRequest;
import com.hospital.boot.app.hospital.dto.HospitalWithDistance;
import com.hospital.boot.domain.hospital.model.vo.Hospital;

import java.util.List;

public interface HospitalService {

    List<Hospital> findHospitalByDepartments(List<String> departments);

    List<HospitalWithDistance> findHospitalByDepartmentsWithDistance(HospitalSearchRequest request);

    List<Hospital> findEmergencyHospital();
}
