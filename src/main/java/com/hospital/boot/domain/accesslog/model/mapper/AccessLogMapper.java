package com.hospital.boot.domain.accesslog.model.mapper;

import com.hospital.boot.domain.accesslog.model.vo.AccessLog;
import org.apache.ibatis.annotations.Mapper;
import java.util.Map;
import java.util.List;

@Mapper
public interface AccessLogMapper {
	
    // 회원 접속 로그 저장
    int insertAccessLog(AccessLog accessLog);

    // 관리자 일별 로그인 횟수 조회 (최근 7일 기준)
    List<Map<String, Object>> getDailyLoginCount();

    // 오래된 접속 로그 삭제
    int deleteOldAccessLogs();
}