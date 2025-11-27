package com.hospital.boot.domain.community.model.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hospital.boot.app.community.dto.*;
import com.hospital.boot.domain.community.model.vo.*;

@Mapper
public interface CommunityMapper {

    List<CommunityPostDto> selectPostList(
            @Param("category") String category,
            @Param("keyword") String keyword);

    CommunityPostDto selectPostDetail(@Param("postId") Long postId);

    int insertPost(Community post);

    List<CommunityCommentDto> selectCommentList(@Param("postId") Long postId);

    int insertComment(CommunityComment comment);

    int deletePost(
            @Param("postId") Long postId,
            @Param("memberId") String memberId);

    int countPostsByMember(String memberId);

    int increaseViews(Long postId);

    int updatePost(Community post);

    int adminDeletePost(@Param("postId") Long postId);

    int insertReport(CommunityReport report);

    List<CommunityReportDto> selectReportList(
            @Param("status") String status,
            @Param("targetType") String targetType);

    CommunityReportDto selectReportDetail(@Param("reportId") Long reportId);

    int updateReportStatus(
            @Param("reportId") Long reportId,
            @Param("status") String status,
            @Param("penaltyReason") String penaltyReason);

    int deleteReport(@Param("reportId") Long reportId);
}