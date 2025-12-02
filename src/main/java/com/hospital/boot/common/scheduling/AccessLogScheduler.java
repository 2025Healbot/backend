package com.hospital.boot.common.scheduling;

import com.hospital.boot.domain.accesslog.model.service.AccessLogService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessLogScheduler {

    private final AccessLogService accessLogService;

    @PostConstruct
    public void onStartup() {
        log.info("애플리케이션 시작 시 오래된 접속 로그 삭제");
        deleteOldLogs();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledDelete() {
        log.info("오래된 접속 로그 삭제 스케줄러 실행");
        deleteOldLogs();
    }

    private void deleteOldLogs() {
        int result = accessLogService.deleteOldAccessLogs();
        log.info("총 {}개의 오래된 접속 로그가 삭제되었습니다.", result);
    }
}
