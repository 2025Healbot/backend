package com.hospital.boot.domain.community.model.service;

import java.util.List;

import com.hospital.boot.app.community.dto.CommunityCommentDto;
import com.hospital.boot.app.community.dto.CommunityCommentWriteRequest;
import com.hospital.boot.app.community.dto.CommunityPostDto;
import com.hospital.boot.app.community.dto.CommunityPostWriteRequest;

public interface CommunityService {

	List<CommunityPostDto> getPostList(String category, String keyword);

    CommunityPostDto getPostDetail(Long postId);

    List<CommunityCommentDto> getComments(Long postId);

    Long writePost(String memberId, CommunityPostWriteRequest req);

    Long writeComment(String memberId, Long postId, CommunityCommentWriteRequest req);

}
