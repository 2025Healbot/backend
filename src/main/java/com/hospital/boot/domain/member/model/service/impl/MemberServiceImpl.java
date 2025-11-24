package com.hospital.boot.domain.member.model.service.impl;

import com.hospital.boot.domain.member.model.service.MemberService;
import com.hospital.boot.app.member.dto.ProfileUpdateRequest;
import com.hospital.boot.domain.member.model.mapper.MemberMapper;
import com.hospital.boot.domain.member.model.vo.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${oauth.redirect-uri}")
    private String redirectUri;

    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;

    @Value("${oauth.naver.client-id}")
    private String naverClientId;

    @Value("${oauth.naver.client-secret}")
    private String naverClientSecret;

    @Override
    public String getSocialUserId(String type, String code) {
        switch (type.toLowerCase()) {
            case "kakao":
                return getKakaoUserId(code);
            case "naver":
                return getNaverUserId(code);
            default:
                throw new IllegalArgumentException("지원하지 않는 소셜 로그인 타입입니다: " + type);
        }
    }
    /**
     * 카카오 사용자 ID 가져오기
     */
    private String getKakaoUserId(String code) {
        try {
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

            if (tokenResponse.getBody() == null) {
                throw new RuntimeException("Failed to get token response from Kakao");
            }

            String accessToken = (String) tokenResponse.getBody().get("access_token");

            if (accessToken == null) {
                throw new RuntimeException("Failed to get access token from Kakao: " + tokenResponse.getBody());
            }

            // 2. 사용자 정보 가져오기
            String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

            HttpHeaders userHeaders = new HttpHeaders();
            userHeaders.setBearerAuth(accessToken);

            HttpEntity<String> userRequest = new HttpEntity<>(userHeaders);
            ResponseEntity<Map> userResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userRequest, Map.class);

            if (userResponse.getBody() == null || userResponse.getBody().get("id") == null) {
                throw new RuntimeException("Failed to get user info from Kakao");
            }

            return String.valueOf(userResponse.getBody().get("id"));
        } catch (Exception e) {
            System.err.println("Kakao login error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Kakao login failed: " + e.getMessage(), e);
        }
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

            if (userResponse.getBody() == null || userResponse.getBody().get("response") == null) {
                throw new RuntimeException("Failed to get user info from Naver");
            }

            Map<String, Object> response = (Map<String, Object>) userResponse.getBody().get("response");

            if (response == null || response.get("id") == null) {
                throw new RuntimeException("Failed to get user id from Naver response");
            }

            return (String) response.get("id");
        } catch (Exception e) {
            System.err.println("Naver login error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Naver login failed: " + e.getMessage(), e);
        }
    }


    @Override
    public Member findBySocialId(String socialId) {
        return mMapper.findBySocialId(socialId);
    }

    @Override
    public Member findByMemberId(String memberId) {
        return mMapper.findByIdOnly(memberId);
    }

    @Override
    public Member findByMemberIdAny(String memberId) {
        return mMapper.findByIdAny(memberId);
    }

    @Override
    public int signup(Member member) {
        // 일반 로그인 회원인 경우 비밀번호 암호화
        if ("normal".equals(member.getLoginType()) && member.getPassword() != null) {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        }
        return mMapper.insertMember(member);
    }

    @Override
    public boolean checkId(String id) {
        int count = mMapper.checkIdExists(id);
        return count == 0; // 0이면 사용 가능(true), 1이상이면 사용 불가(false)
    }

    @Override
    public String findId(String name, String email) {
        return mMapper.findId(name, email);
    }

    @Override
    public Member normalLogin(String memberId, String password) {
        // 아이디로 회원 조회
        Member member = mMapper.findByIdOnly(memberId);

        // 회원이 존재하고 비밀번호가 일치하는지 확인
        if (member != null && passwordEncoder.matches(password, member.getPassword())) {
            return member;
        }

        return null;
    }

    @Override
    public boolean verifyIdAndEmail(String memberId, String email) {
        Member member = mMapper.findByIdAndEmail(memberId, email);
        return member != null;
    }

    @Override
    public boolean resetPassword(String memberId, String password) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);
        int result = mMapper.updatePassword(memberId, encodedPassword);
        return result > 0;
    }
    @Override
    public Member findById(String memberId) {
        // 이미 있는 메서드 재활용 (로그인 아이디 기준 조회)
        return mMapper.findByIdOnly(memberId);
    }

    @Override
    public void updateProfile(String memberId, ProfileUpdateRequest request) {

        // 1) DB에서 기존 회원 정보 가져오기
        Member member = mMapper.findByIdOnly(memberId);
        if (member == null) {
            throw new IllegalArgumentException("회원 정보를 찾을 수 없습니다.");
        }

        // 2) 값 수정
        member.setUserName(request.getUserName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setBornDate(request.getBornDate());
        member.setGender(request.getGender());
        member.setAddress(request.getAddress());

        // 3) 업데이트 쿼리 호출	
        mMapper.updateProfile(member);
    }
	@Override
	public boolean deleteMember(String memberId) {
		// TODO Auto-generated method stub
		int result = mMapper.deleteMember(memberId);
        return result > 0;
	}
    
    
}
