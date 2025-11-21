package com.hospital.boot.domain.accesslog.model.mapper;

import com.hospital.boot.domain.accesslog.model.vo.AccessLog;
import org.apache.ibatis.annotations.Mapper;
import java.util.Map;
import java.util.List;

@Mapper
public interface AccessLogMapper {
    // 접속 로그 저장
    int insertAccessLog(AccessLog accessLog);

    // 일별 로그인 횟수 조회 (최근 7일)
    List<Map<String, Object>> getDailyLoginCount();
}
