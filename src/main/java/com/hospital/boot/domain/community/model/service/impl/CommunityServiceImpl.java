package com.hospital.boot.domain.community.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.hospital.boot.app.community.dto.*;
import com.hospital.boot.domain.community.model.mapper.CommunityMapper;
import com.hospital.boot.domain.community.model.service.CommunityService;
import com.hospital.boot.domain.community.model.vo.*;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityMapper cMapper;

    @Override
    public List<CommunityPostDto> getPostList(String category, String keyword) {
        return cMapper.selectPostList(category, keyword);
    }

    @Override
    public CommunityPostDto getPostDetail(Long postId) {
        return cMapper.selectPostDetail(postId);
    }

    @Override
    public Long writePost(String memberId, CommunityPostWriteRequest req) {
        Community post = new Community();
        post.setMemberId(memberId);
        post.setCategory(req.getCategory());
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());

        cMapper.insertPost(post);
        return post.getPostId();
    }

    @Override
    public List<CommunityCommentDto> getComments(Long postId) {
        return cMapper.selectCommentList(postId);
    }

    @Override
    public Long writeComment(String memberId, Long postId, CommunityCommentWriteRequest req) {
        CommunityComment c = new CommunityComment();
        c.setPostId(postId);
        c.setMemberId(memberId);
        c.setContent(req.getContent());

        cMapper.insertComment(c);
        return c.getCommentId();
    }

	@Override
	public boolean deletePost(Long postId, String memberId) {
		int affected = cMapper.deletePost(postId, memberId);
		return affected > 0;
	}

	@Override
	public int getMyPostCount(String memberId) {
		// TODO Auto-generated method stub
		return cMapper.countPostsByMember(memberId);
	}
    
}