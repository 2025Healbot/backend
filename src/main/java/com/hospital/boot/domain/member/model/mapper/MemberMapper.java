package com.hospital.boot.domain.member.model.mapper;

import com.hospital.boot.domain.member.model.vo.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    Member findBySocialId(String socialId);

    int insertMember(Member member);
}
