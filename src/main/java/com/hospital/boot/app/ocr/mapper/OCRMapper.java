package com.hospital.boot.app.ocr.mapper;

import com.hospital.boot.app.ocr.dto.HospitalOcrInfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OCRMapper {
	// ✅ OCR 텍스트에서 찾은 병원명으로 검색
	List<HospitalOcrInfo> searchHospitalByNameForOCR(@Param("nameKeyword") String nameKeyword);
}
