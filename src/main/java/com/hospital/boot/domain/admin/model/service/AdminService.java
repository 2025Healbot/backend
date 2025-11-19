package com.hospital.boot.domain.admin.model.service;

import com.hospital.boot.domain.member.model.vo.Member;
import java.util.List;

public interface AdminService {

    // 전체 회원 조회
    List<Member> getAllMembers();

    // 회원 수정
    int updateMember(Member member);

    // 회원 삭제
    int deleteMember(String memberId);

}
