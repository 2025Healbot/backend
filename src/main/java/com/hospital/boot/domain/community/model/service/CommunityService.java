package com.hospital.boot.domain.community.model.service;

import java.util.List;

import com.hospital.boot.app.community.dto.*;

public interface CommunityService {

    List<CommunityPostDto> getPostList(String category, String keyword);

    CommunityPostDto getPostDetail(Long postId);

    Long writePost(String memberId, CommunityPostWriteRequest req);

    List<CommunityCommentDto> getComments(Long postId);

    Long writeComment(String memberId, Long postId, CommunityCommentWriteRequest req);

	boolean deletePost(Long postId, String memberId);

	int getMyPostCount(String memberId);

	CommunityPostDto viewPost(Long postId);

	boolean updatePost(Long postId, CommunityPostWriteRequest req);

	boolean adminDeletePost(Long postId);

}