package com.hospital.boot.app.notice.controller;

import com.hospital.boot.domain.notice.model.service.NoticeService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService nService;

}
