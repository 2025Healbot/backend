package com.hospital.boot.app.symptom.controller;

import com.hospital.boot.domain.symptom.model.service.SymptomService;
import com.hospital.boot.domain.symptom.model.vo.Symptom;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/symptom")
@RequiredArgsConstructor
public class SymptomController {

    private final SymptomService sService;

    @GetMapping("")
    public String symptomDetails(@RequestParam String symptom) {
        return sService.symptomDetails(symptom);
    }

}
