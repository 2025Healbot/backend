package com.hospital.boot.domain.hospital.model.mapper;

import com.hospital.boot.domain.hospital.model.vo.Hospital;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HospitalMapper {

    List<Hospital> findHospitalByDepartments(@Param("departments") List<String> departments);

    List<Hospital> findEmergencyHospital();
}
