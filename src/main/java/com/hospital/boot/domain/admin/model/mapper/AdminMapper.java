package com.hospital.boot.domain.admin.model.mapper;

import com.hospital.boot.domain.member.model.vo.Member;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AdminMapper {

    // 전체 회원 조회
    List<Member> selectAllMembers();

    // 회원 수정
    int updateMember(Member member);

    // 회원 삭제
    int deleteMember(String memberId);

}
