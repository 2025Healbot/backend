package com.hospital.boot.app.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateRequest {
    private String userName;
    private String email;
    private String phone;
    private String bornDate;
    private String gender;
    private String address;
    private String createdAt;
}