package com.hospital.boot.domain.accesslog.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AccessLog {
    private int logId;
    private String memberId;
    private String accessDate;
    private String ipAddress;
    private String userAgent;
}
