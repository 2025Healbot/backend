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

    int insertPost(CommunityPost post);

    List<CommunityCommentDto> selectCommentList(@Param("postId") Long postId);

    int insertComment(CommunityComment comment);

    int deletePost(
            @Param("postId") Long postId,
            @Param("memberId") String memberId);

    int countPostsByMember(String memberId);

    int increaseViews(Long postId);

    int updatePost(CommunityPost post);

    int adminDeletePost(@Param("postId") Long postId);

    int insertReport(CommunityReport report);

    List<CommunityReportDto> selectReportList(
            @Param("status") String status,
            @Param("targetType") String targetType);

    CommunityReportDto selectReportDetail(@Param("reportId") Long reportId);

    int updateReportStatus(
            @Param("reportId") Long reportId,
            @Param("status") String status,
            @Param("reply") String reply);

    int updateReply(
            @Param("reportId") Long reportId,
            @Param("reply") String reply);

    int updateReportStatusOnly(
            @Param("reportId") Long reportId,
            @Param("status") String status);

    int deleteReport(@Param("reportId") Long reportId);

    int togglePostVisibility(@Param("postId") Long postId);

    int toggleCommentVisibility(@Param("commentId") Long commentId);

    /** 내가 제재 받은 내역 (내 글/댓글이 신고되어 제재된 것) */
    List<MySanctionDto> selectMyReceivedSanctions(@Param("memberId") String memberId);

    /** 내가 신고해서 제재까지 간 내역 */
    List<MySanctionDto> selectMyReportedSanctions(@Param("memberId") String memberId);

    /** 내가 받은 제재 개수 */
    int selectMySanctionCount(@Param("memberId") String memberId);

    /** 게시글 작성자 ID 조회 */
    String selectPostAuthorId(@Param("postId") Long postId);

    /** 댓글 작성자 ID 조회 */
    String selectCommentAuthorId(@Param("commentId") Long commentId);

    /** 게시글 신고 중복 확인 */
    int checkPostReportExists(
            @Param("postId") Long postId,
            @Param("reporterId") String reporterId);

    /** 댓글 신고 중복 확인 */
    int checkCommentReportExists(
            @Param("commentId") Long commentId,
            @Param("reporterId") String reporterId);
}