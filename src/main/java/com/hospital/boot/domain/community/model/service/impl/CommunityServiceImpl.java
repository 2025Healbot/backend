package com.hospital.boot.domain.community.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Override
	public CommunityPostDto viewPost(Long postId) {
		// TODO Auto-generated method stub
		// 1) 조회수 +1
        cMapper.increaseViews(postId);
        // 2) 방금 변경된 게시글 다시 조회해서 리턴
        return cMapper.selectPostDetail(postId);
	}

	@Override
	public boolean updatePost(Long postId, CommunityPostWriteRequest req) {
		Community post = new Community();
		post.setPostId(postId);
		post.setTitle(req.getTitle());
		post.setContent(req.getContent());
		post.setCategory(req.getCategory());

		int affected = cMapper.updatePost(post);
		return affected > 0;
	}

	@Override
	public boolean adminDeletePost(Long postId) {
		int affected = cMapper.adminDeletePost(postId);
		return affected > 0;
	}
	
	@Override
    @Transactional
    public void reportPost(String reporterId, Long postId, CommunityReportRequest req) {
        CommunityReport r = new CommunityReport();
        r.setTargetType("POST");
        r.setPostId(postId);
        r.setCommentId(null);
        r.setReporterId(reporterId);
        r.setReasonType(req.getReasonType());
        r.setDetail(req.getDetail());
        r.setStatus("PENDING");

        cMapper.insertReport(r);
    }

	@Override
	@Transactional
	public void reportComment(String reporterId, Long commentId, CommunityReportRequest req) {
		CommunityReport r = new CommunityReport();
		r.setTargetType("COMMENT");
		r.setPostId(null);
		r.setCommentId(commentId);
		r.setReporterId(reporterId);
		r.setReasonType(req.getReasonType());
		r.setDetail(req.getDetail());
		r.setStatus("PENDING");

		cMapper.insertReport(r);
	}

	@Override
	public List<CommunityReportDto> getReportList(String status, String targetType) {
		return cMapper.selectReportList(status, targetType);
	}

	@Override
	public CommunityReportDto getReportDetail(Long reportId) {
		return cMapper.selectReportDetail(reportId);
	}

	@Override
	public boolean updateReportStatus(Long reportId, String status, String penaltyReason) {
		int affected = cMapper.updateReportStatus(reportId, status, penaltyReason);
		return affected > 0;
	}

	@Override
	public boolean deleteReport(Long reportId) {
		int affected = cMapper.deleteReport(reportId);
		return affected > 0;
	}
	
	@Override
    public List<MySanctionDto> getMyReceivedSanctions(String memberId) {
        return cMapper.selectMyReceivedSanctions(memberId);
    }

    @Override
    public List<MySanctionDto> getMyReportedSanctions(String memberId) {
        return cMapper.selectMyReportedSanctions(memberId);
    }

    @Override
    public int getMySanctionCount(String memberId) {
        return cMapper.selectMySanctionCount(memberId);
    }

}