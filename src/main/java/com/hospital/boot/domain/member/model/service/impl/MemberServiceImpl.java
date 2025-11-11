package com.hospital.boot.domain.member.model.service.impl;

import com.hospital.boot.domain.member.model.service.MemberService;
import com.hospital.boot.domain.member.model.mapper.MemberMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper mMapper;

}
