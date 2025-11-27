package com.hospital.boot.app.community.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityReportRequest {
	private String reasonType;
	private String detail;
}
