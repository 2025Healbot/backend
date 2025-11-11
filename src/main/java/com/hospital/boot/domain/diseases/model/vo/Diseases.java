package com.hospital.boot.domain.diseases.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Diseases {
    private int diseaseNo;
    private String diseaseName;
    private String imageUrl;
    private String description;
    private int totalPatients;
}
