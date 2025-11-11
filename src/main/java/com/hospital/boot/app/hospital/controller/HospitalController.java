package com.hospital.boot.app.hospital.controller;

import com.hospital.boot.app.hospital.dto.HospitalSearchRequest;
import com.hospital.boot.app.hospital.dto.HospitalWithDistance;
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

    @PostMapping("")
    public List<HospitalWithDistance> findHospital(@RequestBody HospitalSearchRequest request) {
        List<String> departments = request.getDepartments();

        if (departments != null && departments.size() == 1 && departments.get(0).contains(",")) {
            departments = List.of(departments.get(0).trim().split("\\s*,\\s*"));
            request.setDepartments(departments);
        }

        if (request.getLatitude() != null && request.getLongitude() != null) {
            return hService.findHospitalByDepartmentsWithDistance(request);
        }

        List<Hospital> hospitals = hService.findHospitalByDepartments(departments);
        return hospitals.stream()
                .map(h -> new HospitalWithDistance(h, null, null))
                .toList();
    }

    @GetMapping("emergency")
    public List<Hospital> findEmergencyHospital() {
        return hService.findEmergencyHospital();
    }

}
