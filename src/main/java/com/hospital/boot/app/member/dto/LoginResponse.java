package com.hospital.boot.app.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private int success;  // 0: 회원가입 필요, 1: 로그인 성공
    private String socialId;  // 소셜 ID (kakao_123456, naver_123456)
}