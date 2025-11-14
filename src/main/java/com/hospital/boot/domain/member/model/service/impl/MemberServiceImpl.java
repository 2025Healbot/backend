package com.hospital.boot.domain.member.model.service.impl;

import com.hospital.boot.domain.member.model.service.MemberService;
import com.hospital.boot.domain.member.model.mapper.MemberMapper;
import com.hospital.boot.domain.member.model.vo.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper mMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${oauth.redirect-uri}")
    private String redirectUri;

    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;

    @Value("${oauth.naver.client-id}")
    private String naverClientId;

    @Value("${oauth.naver.client-secret}")
    private String naverClientSecret;

    @Value("${oauth.google.client-id}")
    private String googleClientId;

    @Value("${oauth.google.client-secret}")
    private String googleClientSecret;

    @Override
    public String getSocialUserId(String type, String code) {
        switch (type.toLowerCase()) {
            case "kakao":
                return getKakaoUserId(code);
            case "naver":
                return getNaverUserId(code);
            case "google":
                return getGoogleUserId(code);
            default:
                throw new IllegalArgumentException("지원하지 않는 소셜 로그인 타입입니다: " + type);
        }
    }
    /**
     * 카카오 사용자 ID 가져오기
     */
    private String getKakaoUserId(String code) {
        // 1. 액세스 토큰 받기
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, request, Map.class);
        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // 2. 사용자 정보 가져오기
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(accessToken);

        HttpEntity<String> userRequest = new HttpEntity<>(userHeaders);
        ResponseEntity<Map> userResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userRequest, Map.class);

        return String.valueOf(userResponse.getBody().get("id"));
    }

    /**
     * 네이버 사용자 ID 가져오기
     */
    private String getNaverUserId(String code) {
        try {
            // 1. 액세스 토큰 받기 (URL 파라미터 방식)
            String tokenUrl = String.format(
                "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s",
                naverClientId, naverClientSecret, code
            );

            ResponseEntity<Map> tokenResponse = restTemplate.getForEntity(tokenUrl, Map.class);

            String accessToken = (String) tokenResponse.getBody().get("access_token");

            if (accessToken == null) {
                throw new RuntimeException("Failed to get access token from Naver: " + tokenResponse.getBody());
            }

            // 2. 사용자 정보 가져오기
            String userInfoUrl = "https://openapi.naver.com/v1/nid/me";

            HttpHeaders userHeaders = new HttpHeaders();
            userHeaders.set("Authorization", "Bearer " + accessToken);

            HttpEntity<String> userRequest = new HttpEntity<>(userHeaders);
            ResponseEntity<Map> userResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userRequest, Map.class);

            Map<String, Object> response = (Map<String, Object>) userResponse.getBody().get("response");
            return (String) response.get("id");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 구글 사용자 ID 가져오기
     */
    private String getGoogleUserId(String code) {
        // 1. 액세스 토큰 받기
        String tokenUrl = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, request, Map.class);
        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // 2. 사용자 정보 가져오기
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";

        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(accessToken);

        HttpEntity<String> userRequest = new HttpEntity<>(userHeaders);
        ResponseEntity<Map> userResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userRequest, Map.class);

        return (String) userResponse.getBody().get("id");
    }

    @Override
    public Member findBySocialId(String socialId) {
        return mMapper.findBySocialId(socialId);
    }

    @Override
    public int signup(Member member) {
        return mMapper.insertMember(member);
    }

}
