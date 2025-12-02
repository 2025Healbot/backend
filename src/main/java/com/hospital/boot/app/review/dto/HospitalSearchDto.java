package com.hospital.boot.app.review.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HospitalSearchDto {

    // HOSPITAL_ID = VARCHAR2(20) 이므로 String 이 맞음
    private String id;
    private String name;
    private String address;
}