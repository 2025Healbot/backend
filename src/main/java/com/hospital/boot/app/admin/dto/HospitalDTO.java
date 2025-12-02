package com.hospital.boot.app.admin.dto;

import com.hospital.boot.domain.hospital.model.vo.Hospital;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HospitalDTO extends Hospital {
    private String departments; // 진료과 목록 (쉼표로 구분)
}
