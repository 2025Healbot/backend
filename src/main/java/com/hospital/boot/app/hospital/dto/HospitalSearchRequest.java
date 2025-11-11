package com.hospital.boot.app.hospital.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HospitalSearchRequest {
    private List<String> departments;  // 진료과 목록
    private Double latitude;            // 사용자 위도
    private Double longitude;           // 사용자 경도
    private Integer distance;           // 거리 필터 (1, 3, 5 km)
    private String sortBy;              // 정렬 기준 (distance, name 등)
}
