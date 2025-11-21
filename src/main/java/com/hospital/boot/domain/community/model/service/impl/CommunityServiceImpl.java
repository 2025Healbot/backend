package com.hospital.boot.domain.community.model.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.boot.app.community.dto.CommunityCommentDto;
import com.hospital.boot.app.community.dto.CommunityCommentWriteRequest;
import com.hospital.boot.app.community.dto.CommunityPostDto;
import com.hospital.boot.app.community.dto.CommunityPostWriteRequest;
import com.hospital.boot.domain.community.model.mapper.CommunityMapper;
import com.hospital.boot.domain.community.model.service.CommunityService;
import com.hospital.boot.domain.community.model.vo.Community;
import com.hospital.boot.domain.community.model.vo.CommunityComment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityMapper cMapper;

    @Override
    public List<CommunityPostDto> getPostList(String category, String keyword) {
        if (category == null || category.isBlank()) {
            category = "all";
        }
        if (keyword == null) keyword = "";

        List<Community> list = cMapper.selectPostList(category, keyword);

        return list.stream()
                .map(this::toPostDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommunityPostDto getPostDetail(Long postId) {
        cMapper.increaseViewCount(postId);
        Community vo = cMapper.selectPostDetail(postId);
        if (vo == null) {
            return null;
        }
        vo.setViews(vo.getViews() + 1); // 응답에 증가 반영
        return toPostDto(vo);
    }

    @Override
    public List<CommunityCommentDto> getComments(Long postId) {
        List<CommunityComment> list = cMapper.selectComments(postId);
        return list.stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long writePost(String memberId, CommunityPostWriteRequest req) {
        Community vo = new Community();
        vo.setMemberId(memberId);
        vo.setCategory(req.getCategory());
        vo.setTitle(req.getTitle());
        vo.setContent(req.getContent());

        cMapper.insertPost(vo);   // selectKey 로 postId 세팅

        return vo.getPostId();
    }

    @Override
    @Transactional
    public Long writeComment(String memberId, Long postId, CommunityCommentWriteRequest req) {
        CommunityComment cc = new CommunityComment();
        cc.setPostId(postId);
        cc.setMemberId(memberId);
        cc.setContent(req.getContent());

        cMapper.insertComment(cc);   // selectKey 로 commentId 세팅

        return cc.getCommentId();
    }

    // ====== private mapper ======

    private CommunityPostDto toPostDto(Community vo) {
        return CommunityPostDto.builder()
                .postId(vo.getPostId())
                .memberId(vo.getMemberId())
                .category(vo.getCategory())
                .title(vo.getTitle())
                .content(vo.getContent())
                .views(vo.getViews())
                .createdAt(vo.getCreatedAt())
                .updatedAt(vo.getUpdatedAt())
                .status(vo.getStatus())
                .build();
    }

    private CommunityCommentDto toCommentDto(CommunityComment vo) {
        return CommunityCommentDto.builder()
                .commentId(vo.getCommentId())
                .postId(vo.getPostId())
                .memberId(vo.getMemberId())
                .content(vo.getContent())
                .createdAt(vo.getCreatedAt())
                .updatedAt(vo.getUpdatedAt())
                .build();
    }
}