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
    
    /** 내가 제재 받은 내역 (내 글/댓글이 신고되어 제재된 것) */
    List<MySanctionDto> selectMyReceivedSanctions(@Param("memberId") String memberId);

    /** 내가 신고해서 제재까지 간 내역 */
    List<MySanctionDto> selectMyReportedSanctions(@Param("memberId") String memberId);

    /** 내가 받은 제재 개수 */
    int selectMySanctionCount(@Param("memberId") String memberId);
}