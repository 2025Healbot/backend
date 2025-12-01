package com.hospital.boot.app.ocr.mapper;

import com.hospital.boot.app.ocr.dto.HospitalOcrInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OCRMapper {
	HospitalOcrInfo findHospitalForOCR(@Param("hospitalId") String hospitalId);
}
