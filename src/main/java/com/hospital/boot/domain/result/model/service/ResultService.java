package com.hospital.boot.domain.result.model.service;

import java.util.Map;

public interface ResultService {

    // 통합 검색
    Map<String, Object> searchAll(String keyword);
}
