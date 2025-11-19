package com.hospital.boot.domain.member.model.mapper;

import com.hospital.boot.domain.member.model.vo.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    Member findBySocialId(String socialId);

    int insertMember(Member member);

    int checkIdExists(String id);

    String findId(@Param("name") String name, @Param("email") String email);

    Member findByIdAndPassword(@Param("memberId") String memberId, @Param("password") String password);

    Member findByIdOnly(String memberId);

    Member findByIdAndEmail(@Param("memberId") String memberId, @Param("email") String email);

    int updatePassword(@Param("memberId") String memberId, @Param("password") String password);

	void updateProfile(Member member);
}
