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

	void reportPost(String reporterId, Long postId, CommunityReportRequest req);

	void reportComment(String reporterId, Long commentId, CommunityReportRequest req);

	List<CommunityReportDto> getReportList(String status, String targetType);

	CommunityReportDto getReportDetail(Long reportId);

	boolean updateReportStatus(Long reportId, String status, String penaltyReason);

	boolean deleteReport(Long reportId);

	boolean togglePostVisibility(Long postId);

	List<MySanctionDto> getMyReceivedSanctions(String memberId);

    List<MySanctionDto> getMyReportedSanctions(String memberId);

    int getMySanctionCount(String memberId);
}