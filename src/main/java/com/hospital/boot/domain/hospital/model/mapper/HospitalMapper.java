package com.hospital.boot.domain.hospital.model.mapper;

import com.hospital.boot.domain.hospital.model.vo.Hospital;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HospitalMapper {

    // 진료과 리스트로 병원 조회
    List<Hospital> findHospitalByDepartments(@Param("departments") List<String> departments);

    // 진료과 + 지역구로 병원 조회 (최적화)
    List<Hospital> findHospitalByDepartmentsAndDistrict(
            @Param("departments") List<String> departments,
            @Param("district") String district
    );

    List<Hospital> findEmergencyHospital();

    // 모든 병원 조회 (배치용)
    List<Hospital> findAllHospitals();

    // 병원의 NEARBY_DISTRICTS 업데이트
    void updateNearbyDistricts(@Param("hospitalId") String hospitalId, @Param("nearbyDistricts") String nearbyDistricts);
}
