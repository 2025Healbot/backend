package com.hospital.boot.domain.member.model.service;

import com.hospital.boot.domain.member.model.vo.Member;

public interface MemberService {
    String getSocialUserId(String type, String code);
    Member findBySocialId(String socialId);
    int signup(Member member);
    boolean checkId(String id);
    String findId(String name, String email);
    Member normalLogin(String memberId, String password);
    boolean verifyIdAndEmail(String memberId, String email);
    boolean resetPassword(String memberId, String password);
}
