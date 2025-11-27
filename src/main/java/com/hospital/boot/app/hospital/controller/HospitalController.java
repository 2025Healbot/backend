package com.hospital.boot.app.hospital.controller;

import com.hospital.boot.domain.hospital.model.service.HospitalService;
import com.hospital.boot.domain.hospital.model.vo.Hospital;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/hospital")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hService;

    @GetMapping("emergency")
    public List<Hospital> findEmergencyHospital() {
        return hService.findEmergencyHospital();
    }

    @GetMapping("bounds")
    public List<Hospital> findHospitalsByBounds(
            @RequestParam Double swLat,
            @RequestParam Double swLng,
            @RequestParam Double neLat,
            @RequestParam Double neLng,
            @RequestParam(required = false) Boolean emergencyOnly
    ) {
        return hService.findHospitalsByBounds(swLat, swLng, neLat, neLng, emergencyOnly);
    }

    @GetMapping
    public List<Hospital> searchHospitals(
            @RequestParam(required = false) List<String> departments
    ) {
        if (departments == null || departments.isEmpty() || departments.contains("전체")) {
            return hService.findAllHospitals();
        }
        return hService.findByDepartments(departments);
    }

}
