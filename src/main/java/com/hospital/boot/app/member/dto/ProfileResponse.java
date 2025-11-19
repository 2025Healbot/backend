package com.hospital.boot.app.member.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {
	private String userName;
    private String email;
    private String phone;
    private String bornDate; // "1999-01-01"
    private String gender;   // "M" or "F"
    private String address;
    private Date joinDate;
}
