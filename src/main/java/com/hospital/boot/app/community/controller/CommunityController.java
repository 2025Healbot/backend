package com.hospital.boot.app.community.controller;

import com.hospital.boot.domain.community.model.service.CommunityService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService cService;

}
