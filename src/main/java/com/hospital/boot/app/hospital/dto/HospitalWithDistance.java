package com.hospital.boot.app.hospital.dto;

import com.hospital.boot.domain.hospital.model.vo.Hospital;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HospitalWithDistance {
    private Hospital hospital;
    private Integer distance;  // 미터 단위
    private Integer duration;  // 초 단위
}
