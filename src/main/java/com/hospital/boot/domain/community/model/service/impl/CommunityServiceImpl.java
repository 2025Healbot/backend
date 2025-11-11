package com.hospital.boot.domain.community.model.service.impl;

import com.hospital.boot.domain.community.model.service.CommunityService;
import com.hospital.boot.domain.community.model.mapper.CommunityMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityMapper cMapper;

}
