package com.hospital.boot.domain.member.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // 이메일과 인증코드를 임시 저장
    private final Map<String, VerificationCode> verificationCodes = new ConcurrentHashMap<>();

    /**
     * 인증코드 생성 및 이메일 발송
     */
    public boolean sendVerificationCode(String email) {
        try {
            // 6자리 랜덤 인증코드 생성
            String code = generateRandomCode();

            // 이메일 메시지 작성
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("[HealBot] 이메일 인증 코드");
            message.setText(
                "안녕하세요, HealBot 입니다.\n\n" +
                "회원가입을 위한 인증 코드는 다음과 같습니다:\n\n" +
                code + "\n\n" +
                "이 코드는 5분간 유효합니다.\n\n" +
                "본인이 요청하지 않은 경우 이 메일을 무시하시기 바랍니다."
            );

            // 이메일 발송
            mailSender.send(message);

            // 인증코드 저장 (5분 = 300000ms)
            long expiryTime = System.currentTimeMillis() + 300000;
            verificationCodes.put(email, new VerificationCode(code, expiryTime));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 인증코드 검증
     */
    public boolean verifyCode(String email, String code) {
        VerificationCode storedCode = verificationCodes.get(email);

        if (storedCode == null) {
            return false;
        }

        // 만료 시간 체크
        if (System.currentTimeMillis() > storedCode.getExpiryTime()) {
            verificationCodes.remove(email);
            return false;
        }

        // 코드 일치 여부 체크
        if (storedCode.getCode().equals(code)) {
            // 인증 성공 시 저장된 코드 삭제
            verificationCodes.remove(email);
            return true;
        }

        return false;
    }

    /**
     * 6자리 랜덤 숫자 코드 생성
     */
    private String generateRandomCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * 인증코드 정보를 담는 내부 클래스
     */
    private static class VerificationCode {
        private final String code;
        private final long expiryTime;

        public VerificationCode(String code, long expiryTime) {
            this.code = code;
            this.expiryTime = expiryTime;
        }

        public String getCode() {
            return code;
        }

        public long getExpiryTime() {
            return expiryTime;
        }
    }
}
