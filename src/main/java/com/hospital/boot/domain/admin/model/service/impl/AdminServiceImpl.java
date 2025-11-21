package com.hospital.boot.domain.admin.model.service.impl;

import com.hospital.boot.domain.admin.model.service.AdminService;
import com.hospital.boot.domain.admin.model.mapper.AdminMapper;
import com.hospital.boot.domain.member.model.vo.Member;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminMapper aMapper;

    @Override
    public List<Member> getAllMembers() {
        return aMapper.selectAllMembers();
    }

    @Override
    public int updateMember(Member member) {
        return aMapper.updateMember(member);
    }

    @Override
    public int deleteMember(String memberId) {
        return aMapper.deleteMember(memberId);
    }

}
