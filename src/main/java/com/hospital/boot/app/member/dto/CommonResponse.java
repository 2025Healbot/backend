// src/main/java/com/hospital/boot/app/member/dto/CommonResponse.java
package com.hospital.boot.app.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommonResponse {
    private boolean success;
    private String message;
}
