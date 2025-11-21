package com.hospital.boot.domain.community.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hospital.boot.domain.community.model.vo.Community;
import com.hospital.boot.domain.community.model.vo.CommunityComment;

@Mapper
public interface CommunityMapper {

    List<Community> selectPostList(@Param("category") String category,
                                   @Param("keyword") String keyword);

    Community selectPostDetail(@Param("postId") Long postId);

    int increaseViewCount(@Param("postId") Long postId);

    int insertPost(Community post);

    List<CommunityComment> selectComments(@Param("postId") Long postId);

    int insertComment(CommunityComment comment);
}