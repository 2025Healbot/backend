package com.hospital.boot.domain.diseases.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeaturedDiseases {
    private int featuredDiseasesNo;
    private String diseaseName;
    private int displayOrder;
    private String createdAt;
}
