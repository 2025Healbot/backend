package com.hospital.boot.domain.hospital.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Hospital {
    private String hospitalId;		// 병원ID
    private String hospitalName;	// 병원명
    private String address;			// 병원 주소
    private String hospitalGrade;	// 병원 등급
    private String hospitalType;	// 병원 유형
    private String details;			// 병원 상세 정보
    private String operatingHours;	// 운영 시간
    private String lunchTime;		// 점심 시간
    private String emergencyYn;		// 응급실 유무
    private String phone;			// 대표 전화번호
    private String erPhone;			// 응급실 전화번호
    private String longitude;		// 경도
    private String latitude;		// 위도
    private String simpleMap;		// 지도
    private String mainImage;		// 대표 이미지
    private String WebsiteURL;		// 병원 사이트
    private String nearbyDistricts;	// 5KM이내 인근 지역
}