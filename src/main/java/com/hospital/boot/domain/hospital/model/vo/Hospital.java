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
    private String hospitalId;
    private String hospitalName;
    private String address;
    private String hospitalGrade;
    private String hospitalType;
    private String details;
    private String operatingHours;
    private String lunchTime;
    private String emergencyYn;
    private String phone;
    private String longitude;
    private String latitude;
    private String simpleMap;
    private String mainImage;
    private String WebsiteURL;
    private String nearbyDistricts;  // 5km 이내 지역구 목록
}
