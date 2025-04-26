package com.springboot.blog.service;

import com.springboot.blog.DTO.CommentsDTO;
import com.springboot.blog.model.Comment;

import java.util.List;

public interface CommentService {

    public CommentsDTO createComment(Long id, CommentsDTO commentsDTO);
    public List<CommentsDTO> findAllCommentsByPostId(Long postId);
    public CommentsDTO findById(Long commentId, Long PostId);
    public CommentsDTO updateComment(Long postId, Long commentId, CommentsDTO commentsDTO);
    public String deleteCommentById(Long postId, Long commentId);
}
