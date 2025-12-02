package com.hospital.boot.domain.accesslog.model.service.impl;

import com.hospital.boot.domain.accesslog.model.mapper.AccessLogMapper;
import com.hospital.boot.domain.accesslog.model.service.AccessLogService;
import com.hospital.boot.domain.accesslog.model.vo.AccessLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessLogServiceImpl implements AccessLogService {

    private final AccessLogMapper mapper;

    @Override
    public int saveAccessLog(AccessLog accessLog) {
        return mapper.insertAccessLog(accessLog);
    }

    @Override
    public List<Map<String, Object>> getDailyLoginCount() {
        return mapper.getDailyLoginCount();
    }

    @Transactional
    @Override
    public int deleteOldAccessLogs() {
        return mapper.deleteOldAccessLogs();
    }
}
