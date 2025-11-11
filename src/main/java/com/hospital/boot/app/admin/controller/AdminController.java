package com.hospital.boot.app.admin.controller;

import com.hospital.boot.domain.admin.model.service.AdminService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService aService;

}
