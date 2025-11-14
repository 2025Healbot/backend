package com.hospital.boot.domain.member.model.service;

import com.hospital.boot.domain.member.model.vo.Member;

public interface MemberService {

    String getSocialUserId(String type, String code);

    /**
     * 소셜 ID로 회원 조회
     * @param socialId 소셜 ID (type_userId 형식)
     * @return Member 객체 (없으면 null)
     */
    Member findBySocialId(String socialId);

    /**
     * 회원가입
     * @param member 회원 정보
     * @return 등록된 행 수
     */
    int signup(Member member);
}
