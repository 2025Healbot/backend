package com.hospital.boot.domain.hospital.model.mapper;

import com.hospital.boot.domain.hospital.model.vo.Hospital;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HospitalMapper {

    List<Hospital> findHospitalByDepartments(@Param("departments") List<String> departments);

    List<Hospital> findEmergencyHospital();

    // 지도 영역 기반 병원 조회
    List<Hospital> findHospitalsByBounds(
        @Param("swLat") Double swLat,
        @Param("swLng") Double swLng,
        @Param("neLat") Double neLat,
        @Param("neLng") Double neLng,
        @Param("emergencyOnly") Boolean emergencyOnly
    );

    // 관리자용 CRUD
    List<Hospital> getAllHospitals();

    void insertHospital(Hospital hospital);

    void updateHospital(Hospital hospital);

    void deleteHospital(@Param("hospitalId") String hospitalId);

    // 병원 진료과 목록 조회
    List<String> getDepartmentsByHospitalId(@Param("hospitalId") String hospitalId);
}
